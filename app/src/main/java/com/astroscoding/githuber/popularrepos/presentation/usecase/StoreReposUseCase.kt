package com.astroscoding.githuber.popularrepos.presentation.usecase

import com.astroscoding.githuber.common.domain.model.Repo
import com.astroscoding.githuber.common.domain.repository.PopularRepositoriesRepository
import javax.inject.Inject

class StoreReposUseCase @Inject constructor(
    private val repository: PopularRepositoriesRepository
) {

    suspend operator fun invoke(repos: List<Repo>){
        repository.storeRepos(repos)
    }

}