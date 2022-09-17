package com.astroscoding.githuber.search.presentation

import com.astroscoding.githuber.common.domain.model.Repo

data class SearchReposState(
    val loading: Boolean = false,
    val repos: List<Repo> = emptyList(),
    val errorMessage: String = "",
    val isLoadingMoreItems: Boolean = false
)
