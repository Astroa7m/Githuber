package com.astroscoding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.astroscoding.common.presentation.Destination
import com.astroscoding.popularrepos.presentation.PopularReposViewModel
import com.astroscoding.popularrepos.presentation.comp.PopularReposComposable

fun NavGraphBuilder.popularNavGraph(
    paddingValues: PaddingValues,
    popularReposViewModel: PopularReposViewModel
) {
    navigation(
        startDestination = Destination.PopularRepos.route,
        route = Destination.POPULAR_REPO_GRAPH
    ) {
        composable(Destination.PopularRepos.route) {
            PopularReposComposable(
                paddingValues = paddingValues,
                viewModel = popularReposViewModel
            )
        }
    }
}