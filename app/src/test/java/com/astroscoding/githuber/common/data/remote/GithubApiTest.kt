package com.astroscoding.githuber.common.data.remote

import com.astroscoding.githuber.common.util.Constants.DEFAULT_PAGE
import com.astroscoding.githuber.common.util.Constants.DEFAULT_PER_PAGE
import com.astroscoding.githuber.common.util.Constants.DEFAULT_QUERY
import com.astroscoding.githuber.common.util.Constants.DEFAULT_SORT_TYPE
import com.astroscoding.githuber.common.util.Constants.PAGE_PARAM
import com.astroscoding.githuber.common.util.Constants.PER_PAGE_PARAM
import com.astroscoding.githuber.common.util.Constants.Q_PARAM
import com.astroscoding.githuber.common.util.Constants.REPOSITORIES_ENDPOINT
import com.astroscoding.githuber.common.util.Constants.SORT_PARAM
import com.astroscoding.githuber.common.data.remote.model.RepositoriesResultDto
import com.astroscoding.githuber.common.util.JsonReader
import com.astroscoding.githuber.common.util.TestFunctions
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@OptIn(ExperimentalCoroutinesApi::class)
class GithubApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var githubApiService: GithubApi
    private val PATH = "/${REPOSITORIES_ENDPOINT}?${Q_PARAM}=$DEFAULT_QUERY&" +
            "$SORT_PARAM=${DEFAULT_SORT_TYPE.sort}&$PAGE_PARAM=$DEFAULT_PAGE&" +
            "$PER_PAGE_PARAM=$DEFAULT_PER_PAGE"

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        githubApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }

    @Test
    fun test_api_with_success_response() = runTest {
        val responseString = JsonReader.getJson("success_kotlin_repos_response.json")
        val mockedResponse = MockResponse()
            .setBody(responseString)
        mockWebServer.enqueue(mockedResponse)

        val serviceResponse = githubApiService.getPopularGithubRepos()
        val request = mockWebServer.takeRequest()
        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo(PATH)
        assertThat(serviceResponse.code()).isEqualTo(200)
        assertThat(serviceResponse.body()).isEqualTo(
            TestFunctions.getObjectFromJson<RepositoriesResultDto>(
                responseString
            )
        )
    }

    @Test
    fun test_api_with_failed_response() = runTest {
        val mockedResponse = MockResponse()
            .setBody("{}")
            .setResponseCode(404)
        mockWebServer.enqueue(mockedResponse)
        val serviceResponse = githubApiService.getPopularGithubRepos()
        val request = mockWebServer.takeRequest()
        assertThat(request.method).isEqualTo("GET")
        assertThat(serviceResponse.isSuccessful).isFalse()
        assertThat(serviceResponse.code()).isEqualTo(404)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

}