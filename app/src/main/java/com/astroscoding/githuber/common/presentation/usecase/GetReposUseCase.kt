package com.astroscoding.githuber.common.presentation.usecase

import com.astroscoding.githuber.common.domain.model.Repo
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.domain.repository.RepositoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetReposUseCase @Inject constructor(
    private val repository: RepositoriesRepository
) {
    suspend operator fun invoke(
        query: String,
        sort: Sort,
        page: Int
    ): List<Repo> {
        return repository.getRepoRemote(
            query = query,
            sort = sort,
            page = page,
        )
    }

    operator fun invoke(sort: Sort, query: String=""): Flow<List<Repo>>{
        return repository.getLocalRepos(sort, query)
    }

}