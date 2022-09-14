package com.astroscoding.githuber.popularrepos.presentation.comp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.astroscoding.githuber.common.presentation.comp.ReposComposable
import com.astroscoding.githuber.popularrepos.presentation.PopularReposUIEvent
import com.astroscoding.githuber.popularrepos.presentation.PopularReposViewModel

@Composable
fun PopularReposComposable(
    viewModel: PopularReposViewModel,
    paddingValues: PaddingValues
) {
    val state = viewModel.state.collectAsState().value
    // whenever the sort change we gonna animate to the start of the list
    val animate = viewModel.shouldAnimateToStartOfTheList.collectAsState().value
    ReposComposable(
        modifier = Modifier.padding(paddingValues),
        repos = state.repos,
        loading = state.loading,
        errorMessage = state.errorMessage,
        onRefresh = { viewModel.onEvent(PopularReposUIEvent.RefreshRepos) },
        onLastItemReached = { viewModel.onEvent(PopularReposUIEvent.RequestMoreRepos) },
        animate = animate,
        onDoneAnimating = viewModel::onDoneAnimatingToStartOfList
    )
}