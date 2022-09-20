package com.astroscoding.common.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.astroscoding.githuber.common.data.local.RepositoryDatabase
import com.astroscoding.githuber.common.util.Constants
import com.astroscoding.common.util.JsonReader
import com.astroscoding.common.util.TestFunctions.generateReposDomain
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.domain.repository.RepositoriesRepository
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class RepositoriesRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val taskRuler = InstantTaskExecutorRule()

    @Inject
    lateinit var database: RepositoryDatabase
    @Inject
    lateinit var mockWebServer: MockWebServer
    @Inject
    lateinit var repository: RepositoriesRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testSearchForRepos_returnsResponseMatchingParams() = runTest {
        val responseString = JsonReader.getJson("success_kotlin_repos_response.json")
        val mockedResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseString)
        mockWebServer.enqueue(mockedResponse)

        val result = repository.getRepoRemote(
            Constants.DEFAULT_QUERY,
            Constants.DEFAULT_SORT_TYPE,
            page = Constants.DEFAULT_PAGE,
            perPage = Constants.DEFAULT_PER_PAGE
        )
        assertThat(result).hasSize(Constants.DEFAULT_PER_PAGE)
        assertThat(result.sortedByDescending { it.starsCount }).isEqualTo(result)
        assertThat(result.random().language.lowercase()).isEqualTo(Constants.DEFAULT_LANGUAGE)
    }

    @Test
    fun testStoreRepos_returnsTheSameStoredElements() = runTest {
        val repos = generateReposDomain(3)
        repository.storeRepos(repos = repos)
        repository.getLocalRepos(Sort.Stars, "").test {
            assertThat(awaitItem()).containsExactlyElementsIn(repos)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getReposFromRemoteAndSaveThemInDatabase() = runTest {
        val responseString = JsonReader.getJson("success_kotlin_repos_response.json")
        val mockedResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseString)
        mockWebServer.enqueue(mockedResponse)

        val repos = repository.getRepoRemote(
            Constants.DEFAULT_QUERY,
            Constants.DEFAULT_SORT_TYPE,
            Constants.DEFAULT_PAGE,
            Constants.DEFAULT_PER_PAGE
        )
        repository.storeRepos(repos)
        repository.getLocalRepos(Sort.Stars, "").test {
            assertThat(awaitItem()).hasSize(repos.size)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @After
    fun teardown() {
        mockWebServer.shutdown()
        database.close()
    }
}