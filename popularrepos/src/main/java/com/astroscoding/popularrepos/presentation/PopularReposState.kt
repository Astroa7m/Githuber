package com.astroscoding.popularrepos.presentation

import com.astroscoding.common.domain.model.Repo


data class PopularReposState(
    val loading: Boolean = false,
    val repos: List<Repo> = emptyList(),
    val errorMessage: String = "",
    val isLoadingMoreItems: Boolean = false
)
