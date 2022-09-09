package com.astroscoding.githuber.popularrepos.presentation

sealed class PopularReposUIEvent  {
    object RefreshRepos: PopularReposUIEvent()
    object RequestMoreRepos: PopularReposUIEvent()
}
