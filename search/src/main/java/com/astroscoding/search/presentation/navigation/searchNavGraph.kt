package com.astroscoding.search.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.astroscoding.common.domain.model.Repo
import com.astroscoding.common.presentation.Destination
import com.astroscoding.search.presentation.SearchReposViewModel
import com.astroscoding.search.presentation.comp.SearchRepoComposable

fun NavGraphBuilder.searchNavGraph(
    searchReposViewModel: SearchReposViewModel,
    onRepoClicked: (repo: Repo) -> Unit
){
    navigation(
        startDestination = Destination.SearchRepos.route,
        route = Destination.SEARCH_REPO_GRAPH
    ) {
        composable(Destination.SearchRepos.route) {
            SearchRepoComposable(
                viewModel = searchReposViewModel,
                onRepoClicked = onRepoClicked
            )
        }
    }
}