package com.astroscoding.githuber.popularrepos.presentation.usecase

import com.astroscoding.githuber.common.domain.repository.PopularRepositoriesRepository
import javax.inject.Inject


class DeleteAllReposUseCase @Inject constructor(private val repository: PopularRepositoriesRepository) {

    suspend operator fun invoke(){
        repository.deleteAllRepos()
    }

}