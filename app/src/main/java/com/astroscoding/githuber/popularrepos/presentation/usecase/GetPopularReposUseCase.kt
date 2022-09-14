package com.astroscoding.githuber.popularrepos.presentation.usecase

import com.astroscoding.githuber.common.domain.model.Repo
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.domain.repository.PopularRepositoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetPopularReposUseCase @Inject constructor(
    private val repository: PopularRepositoriesRepository
) {
    suspend operator fun invoke(
        query: String,
        sort: Sort,
        page: Int
    ): List<Repo> {
        return repository.getPopularRepoRemote(
            query = query,
            sort = sort,
            page = page,
        )
    }

    operator fun invoke(sort: Sort): Flow<List<Repo>>{
        return repository.getLocalRepos(sort)
    }

}