package com.astroscoding.popularrepos.presentation.comp

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.astroscoding.common.presentation.comp.ReposComposable
import com.astroscoding.popularrepos.presentation.PopularReposUIEvent
import com.astroscoding.popularrepos.presentation.PopularReposViewModel

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
        onRefresh = { viewModel.onEvent(PopularReposUIEvent.RefreshRepos) },
        onLastItemReached = { viewModel.onEvent(PopularReposUIEvent.RequestMoreRepos) },
        animate = animate,
        onDoneAnimating = viewModel::onDoneAnimatingToStartOfList
    )
    //temporarily
    val context = LocalContext.current
    LaunchedEffect(key1 = state.errorMessage) {
        if (state.errorMessage.isNotEmpty())
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
    }
}