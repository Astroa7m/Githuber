package com.astroscoding.githuber.common.data.remote

import com.astroscoding.githuber.common.data.remote.model.RepositoriesResultDto
import com.astroscoding.githuber.common.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET(Constants.REPOSITORIES_ENDPOINT)
    suspend fun getGithubRepos(
        @Query(Constants.Q_PARAM, encoded = true) searchQuery: String = Constants.DEFAULT_QUERY,
        @Query(Constants.SORT_PARAM) sort: String = Constants.DEFAULT_SORT_TYPE.sort /*maybe later change to get preferences for sorting*/,
        @Query(Constants.PAGE_PARAM) page: Int = Constants.DEFAULT_PAGE,
        @Query(Constants.PER_PAGE_PARAM) perPage: Int = Constants.DEFAULT_PER_PAGE
    ): Response<RepositoriesResultDto>

}