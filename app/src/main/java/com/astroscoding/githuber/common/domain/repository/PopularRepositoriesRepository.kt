package com.astroscoding.githuber.common.domain.repository

import com.astroscoding.githuber.common.domain.model.Repo
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.util.Constants
import kotlinx.coroutines.flow.Flow

interface PopularRepositoriesRepository {

    suspend fun storeRepos(repos: List<Repo>)
    fun getLocalRepos(sort: Sort): Flow<List<Repo>>
    suspend fun getPopularRepoRemote(
        query: String,
        sort: Sort,
        page: Int = Constants.DEFAULT_PAGE,
        perPage: Int = Constants.DEFAULT_PER_PAGE
    ): List<Repo>
    suspend fun deleteAllRepos()
}