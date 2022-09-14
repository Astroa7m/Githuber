package com.astroscoding.githuber.popularrepos.presentation

import com.astroscoding.githuber.common.domain.model.Repo

data class PopularReposState(
    val loading: Boolean = false,
    val repos: List<Repo> = emptyList(),
    val errorMessage: String = "",
    val isLoadingMoreItems: Boolean = false
)
