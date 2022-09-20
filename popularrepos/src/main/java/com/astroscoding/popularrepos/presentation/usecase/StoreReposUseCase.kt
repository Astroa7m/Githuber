package com.astroscoding.popularrepos.presentation.usecase

import com.astroscoding.common.domain.model.Repo
import com.astroscoding.common.domain.repository.RepositoriesRepository
import javax.inject.Inject

class StoreReposUseCase @Inject constructor(
    private val repository: RepositoriesRepository
) {

    suspend operator fun invoke(repos: List<Repo>){
        repository.storeRepos(repos)
    }

}