package com.astroscoding.githuber.popularrepos.presentation.usecase

import com.astroscoding.githuber.common.domain.model.Repo
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.domain.repository.PopularRepositoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// TODO: think of this bruh

class GetPopularReposUseCase @Inject constructor(
    private val repository: PopularRepositoriesRepository
) {
    // TODO: handle pagination later
    // first get items from db
    // if there are no items OR sortOrder has Changed OR queryHasChanged OR userRefreshed
    // then fetch new items and put them in db
    // changing of sort order or query will fire of this function again from the viewModel
    suspend operator fun invoke(
        query: String,
        sort: Sort
    ): List<Repo> {
        return repository.getPopularRepoRemote(
            query = query,
            sort = sort
        )
    }

    operator fun invoke(sort: Sort): Flow<List<Repo>>{
        return repository.getLocalRepos(sort)
    }

}