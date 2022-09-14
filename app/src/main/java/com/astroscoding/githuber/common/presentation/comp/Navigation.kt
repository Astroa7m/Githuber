package com.astroscoding.githuber.common.presentation.comp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.astroscoding.githuber.common.presentation.Destination
import com.astroscoding.githuber.popularrepos.presentation.PopularReposViewModel
import com.astroscoding.githuber.popularrepos.presentation.comp.PopularReposComposable
import com.astroscoding.githuber.search.presentation.comp.SearchRepoComposable

@Composable
fun NavController(
    navController: NavHostController,
    paddingValues: PaddingValues,
    popularReposViewModel: PopularReposViewModel
) {
    NavHost(navController = navController, startDestination = Destination.PopularRepos.route){
        composable(Destination.PopularRepos.route){
            PopularReposComposable(paddingValues = paddingValues, viewModel = popularReposViewModel)
        }
        composable(Destination.SearchRepos.route){
            SearchRepoComposable(paddingValues = paddingValues)
        }
    }
}