package com.astroscoding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.astroscoding.common.presentation.Destination
import com.astroscoding.common.presentation.SingleRepoScreen
import com.astroscoding.common.presentation.SingleRepoViewModel
import com.astroscoding.popularrepos.presentation.PopularReposViewModel
import com.astroscoding.popularrepos.presentation.navigation.popularNavGraph
import com.astroscoding.search.presentation.SearchReposViewModel
import com.astroscoding.search.presentation.navigation.searchNavGraph

@Composable
fun GitHuberNavHost(
    navController: NavHostController,
    popularReposViewModel: PopularReposViewModel,
    searchReposViewModel: SearchReposViewModel,
    singleRepoViewModel: SingleRepoViewModel
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Destination.POPULAR_REPO_GRAPH,
        route = Destination.ROOT_GRAPH
    ) {
        popularNavGraph(popularReposViewModel) {
            singleRepoViewModel.setRepo(it)
            navController.navigate(Destination.DetailsRepo.route)
        }
        searchNavGraph(
            searchReposViewModel,
            context = context
        ) {
            singleRepoViewModel.setRepo(it)
            navController.navigate(Destination.DetailsRepo.route)
        }
        composable(
            route = Destination.DetailsRepo.route
        ) {
            SingleRepoScreen(singleRepoViewModel)
        }
    }
}

