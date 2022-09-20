package com.astroscoding.popularrepos.presentation

import com.astroscoding.common.domain.model.Sort

sealed class PopularReposUIEvent  {
    object RefreshRepos: PopularReposUIEvent()
    object RequestMoreRepos: PopularReposUIEvent()
    class SelectNewSort(val newSort: Sort): PopularReposUIEvent()
    class SelectNewLanguage(val newLanguage: String): PopularReposUIEvent()
}
