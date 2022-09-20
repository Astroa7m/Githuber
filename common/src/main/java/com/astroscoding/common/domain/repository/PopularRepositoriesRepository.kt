package com.astroscoding.common.domain.repository

import com.astroscoding.common.domain.model.Repo
import com.astroscoding.common.domain.model.Sort
import com.astroscoding.common.util.Constants
import kotlinx.coroutines.flow.Flow

interface RepositoriesRepository {

    suspend fun storeRepos(repos: List<Repo>)
    fun getLocalRepos(sort: Sort, query: String): Flow<List<Repo>>
    suspend fun getRepoRemote(
        query: String,
        sort: Sort,
        page: Int = Constants.DEFAULT_PAGE,
        perPage: Int = Constants.DEFAULT_PER_PAGE
    ): List<Repo>
    suspend fun deleteAllRepos()
}