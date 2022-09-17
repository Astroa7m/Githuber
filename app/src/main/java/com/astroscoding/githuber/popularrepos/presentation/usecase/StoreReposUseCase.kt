package com.astroscoding.githuber.popularrepos.presentation.usecase

import com.astroscoding.githuber.common.domain.model.Repo
import com.astroscoding.githuber.common.domain.repository.RepositoriesRepository
import javax.inject.Inject

class StoreReposUseCase @Inject constructor(
    private val repository: RepositoriesRepository
) {

    suspend operator fun invoke(repos: List<Repo>){
        repository.storeRepos(repos)
    }

}