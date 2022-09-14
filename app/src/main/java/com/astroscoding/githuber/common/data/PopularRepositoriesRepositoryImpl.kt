package com.astroscoding.githuber.common.data

import com.astroscoding.githuber.common.data.local.RepositoryDatabase
import com.astroscoding.githuber.common.data.local.model.RepositoryEntity
import com.astroscoding.githuber.common.data.remote.GithubApi
import com.astroscoding.githuber.common.domain.model.Repo
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.domain.repository.PopularRepositoriesRepository
import com.astroscoding.githuber.common.util.BadQueryException
import com.astroscoding.githuber.common.util.EmptyResponseBodyException
import com.astroscoding.githuber.common.util.LimitExceededException
import com.astroscoding.githuber.common.util.ResponseUnsuccessfulException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PopularRepositoriesRepositoryImpl @Inject constructor(
    private val api: GithubApi,
    database: RepositoryDatabase
) : PopularRepositoriesRepository {

    private val dao = database.repositoriesDao

    override fun getLocalRepos(sort: Sort): Flow<List<Repo>> {
        return dao.getAllRepos(sort.sort).map { reposEntity ->
            reposEntity.map { repoEntity ->
                repoEntity.mapTo()
            }
        }
    }

    override suspend fun storeRepos(repos: List<Repo>) {
        val reposEntity = repos.map { repo ->
            RepositoryEntity.mapFrom(repo)
        }
        reposEntity.forEach { repoEntity ->
            dao.insertRepo(repoEntity)
        }
    }

    override suspend fun getPopularRepoRemote(
        query: String,
        sort: Sort,
        page: Int,
        perPage: Int
    ): List<Repo> {
            val response = try {
                api.getPopularGithubRepos(
                    searchQuery = query,
                    sort = sort.sort,
                    page = page,
                    perPage = perPage
                )
            } catch (e: Exception){
                throw ResponseUnsuccessfulException()
            }
            if (response.isSuccessful) {
                // user enters weird query or language
                // server responds with 200 but no items
                // so we handle this
                if (response.body()?.total_count == 0)
                    throw BadQueryException()
                return response.body()?.let { body ->
                    body.repositories.map {
                        it.mapTo()
                    }
                } ?: throw EmptyResponseBodyException()
            } else {
                if (response.code()==403) // user requesting too much
                    throw LimitExceededException()
                else (response.code()==422) // bad query
                    throw BadQueryException()
            }
    }
    //403 when error response
    //422

    override suspend fun deleteAllRepos() {
        dao.deleteAllRepos()
    }
}