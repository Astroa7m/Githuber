package com.astroscoding.popularrepos.presentation.usecase

import com.astroscoding.common.domain.repository.RepositoriesRepository
import javax.inject.Inject


class DeleteAllReposUseCase @Inject constructor(private val repository: RepositoriesRepository) {

    suspend operator fun invoke(){
        repository.deleteAllRepos()
    }

}