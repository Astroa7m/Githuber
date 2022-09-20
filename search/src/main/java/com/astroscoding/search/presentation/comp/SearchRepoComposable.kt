package com.astroscoding.search.presentation.comp

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.astroscoding.common.presentation.comp.ReposComposable
import com.astroscoding.search.presentation.SearchReposUIEvent
import com.astroscoding.search.presentation.SearchReposViewModel

@Composable
fun SearchRepoComposable(
    viewModel: SearchReposViewModel,
    paddingValues: PaddingValues
) {
    val state = viewModel.state.collectAsState().value
    val animate = viewModel.shouldAnimateToStartOfTheList.collectAsState().value
    val notSearching = state.repos.isEmpty() && !state.loading
    if (notSearching)
        SearchStart(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues))
    else {
        ReposComposable(
            modifier = Modifier.padding(paddingValues),
            repos = state.repos,
            loading = state.loading,
            onLastItemReached = { viewModel.onEvent(SearchReposUIEvent.LoadMoreRepos) },
            animate = animate,
            onDoneAnimating = viewModel::onDoneAnimatingToStartOfList
        )
    }
    //temporarily
    val context = LocalContext.current
    LaunchedEffect(key1 = state.errorMessage) {
        if (state.errorMessage.isNotEmpty())
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
    }
}