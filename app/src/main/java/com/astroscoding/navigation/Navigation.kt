package com.astroscoding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.astroscoding.common.presentation.Destination
import com.astroscoding.popularrepos.presentation.PopularReposViewModel
import com.astroscoding.search.presentation.SearchReposViewModel

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
        popularNavGraph(paddingValues, popularReposViewModel)
        searchNavGraph(paddingValues, searchReposViewModel)
    }
}
