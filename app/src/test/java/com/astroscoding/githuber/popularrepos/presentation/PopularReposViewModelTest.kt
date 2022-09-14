package com.astroscoding.githuber.popularrepos.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.astroscoding.githuber.common.data.preferences.RepoPreferences
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.domain.repository.PopularRepositoriesRepository
import com.astroscoding.githuber.common.util.Constants
import com.astroscoding.githuber.common.util.ResponseUnsuccessfulException
import com.astroscoding.githuber.common.util.TestFunctions.generateReposDomain
import com.astroscoding.githuber.popularrepos.presentation.usecase.DeleteAllReposUseCase
import com.astroscoding.githuber.popularrepos.presentation.usecase.GetPopularReposUseCase
import com.astroscoding.githuber.popularrepos.presentation.usecase.StoreReposUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
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

    private val newSort = Constants.DEFAULT_SORT_TYPE
    private val defaultQuery = Constants.DEFAULT_QUERY
    private val newLanguage = Constants.DEFAULT_LANGUAGE
    private val coroutineDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: PopularReposViewModel
    private lateinit var repository: PopularRepositoriesRepository
    private val repos = generateReposDomain(count = 3, starsCounts = listOf(5, 8, 2))
    private val reposFlow =
        flowOf(generateReposDomain(count = 5, starsCounts = listOf(5, 1, 3, 4, 10)))

    @Before
    fun setup() {
        Dispatchers.setMain(coroutineDispatcher)
    }

    private fun constructObjectsForScenario_dbHasItems() {
        repository = mock {
            onBlocking { getLocalRepos(newSort) } doReturn reposFlow
            onBlocking { getPopularRepoRemote(newLanguage, newSort) } doReturn repos
        }
        constructRemainingObjects()
    }

    private fun constructObjectsForScenario_dbIsEmpty() {
        repository = mock {
            onGeneric { getLocalRepos(newSort) } doReturn flow {
                emit(emptyList())
                delay(1000)
                emit(repos)
            }
            onBlocking { getPopularRepoRemote(defaultQuery, newSort) } doReturn repos
        }
        constructRemainingObjects()
    }

    private fun constructObjectsForScenario_refreshingEventOccurred() {
        val newRepos = generateReposDomain(2, listOf(56, 1))
        repository = mock {
            onGeneric { getLocalRepos(newSort) } doReturn flow {
                emit(repos)
                delay(500)
                emit(emptyList())
                delay(5000)
                emit(newRepos)
            }
            onBlocking { getPopularRepoRemote(defaultQuery, newSort) } doReturn repos
        }
        constructRemainingObjects()
    }

    private fun constructObjectsForScenario_LoadMoreReposEventOccurred() {
        val newRepos = generateReposDomain(2, listOf(56, 1))
        repository = mock {
            onGeneric { getLocalRepos(newSort) } doReturn flow {
                emit(repos)
                delay(500)
                emit(repos + newRepos)
            }
            onBlocking { getPopularRepoRemote(defaultQuery, newSort) } doReturn repos
        }
        constructRemainingObjects()
    }

    private fun constructObjectsForScenario_LoadReposThrowException() {
        repository = mock {
            onGeneric { getLocalRepos(newSort) } doThrow ResponseUnsuccessfulException()
            onBlocking {
                getPopularRepoRemote(
                    defaultQuery,
                    newSort
                )
            } doThrow ResponseUnsuccessfulException()
        }
        constructRemainingObjects()
    }

    private fun constructRemainingObjects() {
        //getPopularRepoUseCase
        val getPopularRepoUseCase = GetPopularReposUseCase(repository)
        //storeReposUseCase
        val storeReposUseCase = StoreReposUseCase(repository)
        //deleteRepoUseCase
        val deleteReposUseCase = DeleteAllReposUseCase(repository)
        //repoPreferences
        val repoPreferences = mock<RepoPreferences> {
            onGeneric { language } doReturn flowOf(newLanguage)
            onGeneric { sortOrder } doReturn flowOf(newSort)
        }
        viewModel = PopularReposViewModel(
            getPopularReposUseCase = getPopularRepoUseCase,
            storeReposUseCase = storeReposUseCase,
            repoPreferences,
            deleteAllReposUseCase = deleteReposUseCase,
            dispatcher = coroutineDispatcher
        )
    }

    @Test
    fun initializingViewModel_getsRepoFromDb() = runTest {
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
    fun initializingViewModel_initializingSortAndLanguageFromPreferences() {
        constructObjectsForScenario_dbHasItems()
        assertThat(viewModel.lang).isEqualTo(Constants.DEFAULT_LANGUAGE)
        assertThat(viewModel.sort).isEqualTo(Sort.EmptySort)
        coroutineDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.lang).isEqualTo(newLanguage)
        assertThat(viewModel.sort).isEqualTo(newSort)
    }

    @Test
    fun refreshReposEvent_deletesRepos_dbReturnsEmpty() = runTest { //yup! again
        constructObjectsForScenario_refreshingEventOccurred()
        coroutineDispatcher.scheduler.runCurrent()
        assertThat(viewModel.state.value.repos).isNotEmpty()

        viewModel.onEvent(PopularReposUIEvent.RefreshRepos)

        coroutineDispatcher.scheduler.advanceTimeBy(501)
        assertThat(viewModel.state.value.repos).isEmpty()

        coroutineDispatcher.scheduler.advanceTimeBy(5001)
        assertThat(viewModel.state.value.repos).isNotEmpty()
    }

    @Test
    fun loadMoreReposEvent_getsNewRepos_attachesThemToList() = runTest { //yup! again again again
        constructObjectsForScenario_LoadMoreReposEventOccurred()
        coroutineDispatcher.scheduler.runCurrent()
        val listBeforePaginating = viewModel.state.value.repos
        assertThat(listBeforePaginating).isNotEmpty()

        viewModel.onEvent(PopularReposUIEvent.RequestMoreRepos)
        assertThat(viewModel.state.value.loading).isTrue()

        coroutineDispatcher.scheduler.runCurrent()
        assertThat(viewModel.state.value.repos).containsExactlyElementsIn(listBeforePaginating)
    }

    @Test
    fun loadMoreReposEvent_getsNewRepos_incrementsCurrentPage() = runTest { //yup! again again again
        constructObjectsForScenario_LoadMoreReposEventOccurred()
        coroutineDispatcher.scheduler.runCurrent()
        val listBeforePaginating = viewModel.state.value.repos
        assertThat(listBeforePaginating).isNotEmpty()

        viewModel.onEvent(PopularReposUIEvent.RequestMoreRepos)
        assertThat(viewModel.state.value.loading).isTrue()

        coroutineDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.currentPage).isEqualTo(2)
    }

    @Test
    fun `requestingRepos_throwsBigError ^_^`() {
        constructObjectsForScenario_LoadReposThrowException()
        coroutineDispatcher.scheduler.runCurrent()
        assertThat(viewModel.state.value.errorMessage).isEqualTo(ResponseUnsuccessfulException().message)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

}