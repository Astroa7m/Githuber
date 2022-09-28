package com.astroscoding.common.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.astroscoding.common.domain.model.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleRepoViewModel @Inject constructor() : ViewModel() {

    val repo = mutableStateOf<Repo?>(null)

    fun setRepo(repo: Repo) {
        this.repo.value = repo
    }


}