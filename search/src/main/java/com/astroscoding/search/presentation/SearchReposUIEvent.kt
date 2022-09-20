package com.astroscoding.search.presentation

import com.astroscoding.common.domain.model.Sort

sealed class SearchReposUIEvent{
    class InputQuery(val query: String): SearchReposUIEvent()
    class SelectNewSort(val newSort: Sort): SearchReposUIEvent()
    class SelectNewLanguage(val newLanguage: String): SearchReposUIEvent()
    object LoadMoreRepos: SearchReposUIEvent()
    object SearchRepos: SearchReposUIEvent()
    object CloseSearch : SearchReposUIEvent()
}
