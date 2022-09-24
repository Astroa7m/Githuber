package com.astroscoding.popularrepos.presentation.comp

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.astroscoding.common.domain.model.Repo
import com.astroscoding.common.presentation.comp.ReposComposable
import com.astroscoding.popularrepos.presentation.PopularReposUIEvent
import com.astroscoding.popularrepos.presentation.PopularReposViewModel

@Composable
fun PopularReposComposable(
    viewModel: PopularReposViewModel,
    onRepoClicked: (repo: Repo) -> Unit
) {
    val state = viewModel.state.collectAsState().value
    // whenever the sort change we gonna animate to the start of the list
    val animate = viewModel.shouldAnimateToStartOfTheList.collectAsState().value
    ReposComposable(
        repos = state.repos,
        loading = state.loading,
        onRefresh = { viewModel.onEvent(PopularReposUIEvent.RefreshRepos) },
        onLastItemReached = { viewModel.onEvent(PopularReposUIEvent.RequestMoreRepos) },
        animate = animate,
        onDoneAnimating = viewModel::onDoneAnimatingToStartOfList,
        onRepoClicked = onRepoClicked
    )
    //temporarily
    val context = LocalContext.current
    LaunchedEffect(key1 = state.errorMessage) {
        if (state.errorMessage.isNotEmpty())
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
    }
}