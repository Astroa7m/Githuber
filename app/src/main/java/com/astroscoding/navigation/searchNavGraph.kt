package com.astroscoding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.astroscoding.common.presentation.Destination
import com.astroscoding.search.presentation.SearchReposViewModel
import com.astroscoding.search.presentation.comp.SearchRepoComposable

fun NavGraphBuilder.searchNavGraph(
    paddingValues: PaddingValues,
    searchReposViewModel: SearchReposViewModel
){
    navigation(
        startDestination = Destination.SearchRepos.route,
        route = Destination.SEARCH_REPO_GRAPH
    ) {
        composable(Destination.SearchRepos.route) {
            SearchRepoComposable(
                paddingValues = paddingValues,
                viewModel = searchReposViewModel
            )
        }
    }
}