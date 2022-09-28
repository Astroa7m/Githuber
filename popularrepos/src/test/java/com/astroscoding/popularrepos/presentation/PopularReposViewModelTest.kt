package com.astroscoding.popularrepos.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.astroscoding.common.data.preferences.RepoPreferences
import com.astroscoding.common.domain.model.Sort
import com.astroscoding.common.domain.repository.RepositoriesRepository
import com.astroscoding.common.presentation.usecase.GetReposUseCase
import com.astroscoding.common.util.Constants
import com.astroscoding.common.util.exceptions.ResponseUnsuccessfulException
import com.astroscoding.popularrepos.util.TestFunctions.generateReposDomain
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PopularReposViewModelTest {

    private val newSort = Sort.EmptySort
    private val newLanguage = Constants.DEFAULT_LANGUAGE
    private val coroutineDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: PopularReposViewModel
    private lateinit var repository: RepositoriesRepository
    private val repos = generateReposDomain(count = 3, starsCounts = listOf(5, 8, 2))
    private val reposFlow =
        flowOf(generateReposDomain(count = 5, starsCounts = listOf(5, 1, 3, 4, 10)))

    @Before
    fun setup() {
        Dispatchers.setMain(coroutineDispatcher)
    }

    private fun constructObjectsForScenario_dbHasItems() {
        repository = mock {
            onBlocking { getLocalRepos(newSort, newLanguage) } doReturn reposFlow
            onBlocking { getRepoRemote(newLanguage, newSort) } doReturn repos
        }
        constructRemainingObjects()
    }

    private fun constructObjectsForScenario_dbIsEmpty() {
        repository = mock {
            onGeneric { getLocalRepos(newSort, newLanguage) } doReturn flow {
                emit(emptyList())
                delay(1000)
                emit(repos)
            }
            onBlocking { getRepoRemote(newLanguage, newSort) } doReturn repos
        }
        constructRemainingObjects()
    }

    private fun constructObjectsForScenario_refreshingEventOccurred() {
        val newRepos = generateReposDomain(2, listOf(56, 1))
        repository = mock {
            onGeneric { getLocalRepos(newSort, newLanguage) } doReturn flow {
                emit(repos)
                delay(500)
                emit(emptyList())
                delay(5000)
                emit(newRepos)
            }
            onBlocking { getRepoRemote(newLanguage, newSort) } doReturn repos
        }
        constructRemainingObjects()
    }

    private fun constructObjectsForScenario_LoadMoreReposEventOccurred() {
        val newRepos = generateReposDomain(2, listOf(56, 1))
        repository = mock {
            onGeneric { getLocalRepos(newSort, newLanguage) } doReturn flow {
                emit(repos)
                delay(500)
                emit(repos + newRepos)
            }
            onBlocking { getRepoRemote(newLanguage, newSort) } doReturn repos
        }
        constructRemainingObjects()
    }

    private fun constructObjectsForScenario_LoadReposThrowException() {
        repository = mock {
            onGeneric { getLocalRepos(newSort, newLanguage) } doReturn flowOf(emptyList())
            onBlocking {
                getRepoRemote(
                    newLanguage,
                    newSort
                )
            } doThrow ResponseUnsuccessfulException()
        }
        constructRemainingObjects()
    }

    private fun constructRemainingObjects() {
        //getPopularRepoUseCase
        val getRepoUseCase = GetReposUseCase(repository)
        //storeReposUseCase
        val storeReposUseCase =
            com.astroscoding.popularrepos.presentation.usecase.StoreReposUseCase(repository)
        //deleteRepoUseCase
        val deleteReposUseCase =
            com.astroscoding.popularrepos.presentation.usecase.DeleteAllReposUseCase(repository)
        //repoPreferences
        val repoPreferences = mock<RepoPreferences> {
            onGeneric { language } doReturn flowOf(newLanguage)
            onGeneric { sortOrder } doReturn flowOf(newSort)
        }
        viewModel = PopularReposViewModel(
            getReposUseCase = getRepoUseCase,
            storeReposUseCase = storeReposUseCase,
            repoPreferences = repoPreferences,
            deleteAllReposUseCase = deleteReposUseCase,
            dispatcher = coroutineDispatcher
        )
    }

    @Test
    fun initializingViewModel_getsRepoFromDb()  {
        constructObjectsForScenario_dbHasItems()
        coroutineDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.state.value.repos).isNotEmpty()
    }

    @Test
    fun initializingViewModel_whenDbIsEmpty_getsRepoFromRemoteSource() =
        runTest { //this handles the fake flow delays
            constructObjectsForScenario_dbIsEmpty()
            assertThat(viewModel.state.value.repos).isEmpty()
            coroutineDispatcher.scheduler.advanceTimeBy(1500)
            assertThat(viewModel.state.value.repos).isEqualTo(repos)
        }


    @Test
    fun initializingViewModel_initializingSortAndLanguageFromPreferences() = runTest{
        constructObjectsForScenario_dbHasItems()
        val collectionJob = launch(UnconfinedTestDispatcher()) {
            launch {
                viewModel.language.collect()
            }
            launch {
                viewModel.sort.collect()
            }
        }
        assertThat(viewModel.language.value).isEqualTo(Constants.DEFAULT_LANGUAGE)
        assertThat(viewModel.sort.value).isEqualTo(Sort.EmptySort)
        coroutineDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.language.value).isEqualTo(newLanguage)
        assertThat(viewModel.sort.value).isEqualTo(newSort)
        collectionJob.cancel()
    }

    @Test
    fun refreshReposEvent_deletesRepos_dbReturnsEmpty() = runTest { //yup! again
        constructObjectsForScenario_refreshingEventOccurred()
        coroutineDispatcher.scheduler.runCurrent()
        assertThat(viewModel.state.value.repos).isNotEmpty()

        viewModel.onEvent(com.astroscoding.popularrepos.presentation.PopularReposUIEvent.RefreshRepos)

        coroutineDispatcher.scheduler.advanceTimeBy(501)
        assertThat(viewModel.state.value.repos).isEmpty()

        coroutineDispatcher.scheduler.advanceTimeBy(5001)
        assertThat(viewModel.state.value.repos).isNotEmpty()
    }

    @Test
    fun loadMoreReposEvent_getsNewRepos_attachesThemToList() = runTest { //yup! again again again
        constructObjectsForScenario_LoadMoreReposEventOccurred()
        coroutineDispatcher.scheduler.advanceUntilIdle()
        val listBeforePaginating = viewModel.state.value.repos
        assertThat(listBeforePaginating).isNotEmpty()

        viewModel.onEvent(com.astroscoding.popularrepos.presentation.PopularReposUIEvent.RequestMoreRepos)
        assertThat(viewModel.state.value.loading).isTrue()

        coroutineDispatcher.scheduler.runCurrent()
        assertThat(viewModel.state.value.repos).containsExactlyElementsIn(listBeforePaginating)
    }

    @Test
    fun `requestingRepos_throwsBigError ^_^`() {
        constructObjectsForScenario_LoadReposThrowException()
        coroutineDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.state.value.errorMessage).isEqualTo(ResponseUnsuccessfulException().message)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

}