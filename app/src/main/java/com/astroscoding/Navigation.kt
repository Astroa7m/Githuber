package com.astroscoding

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.astroscoding.common.presentation.Destination
import com.astroscoding.popularrepos.presentation.PopularReposViewModel
import com.astroscoding.popularrepos.presentation.comp.PopularReposComposable
import com.astroscoding.search.presentation.SearchReposViewModel
import com.astroscoding.search.presentation.comp.SearchRepoComposable

@Composable
fun GitHuberNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    popularReposViewModel: PopularReposViewModel,
    searchReposViewModel: SearchReposViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Destination.POPULAR_REPO_GRAPH
    ) {
        // popular repo graph
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
}