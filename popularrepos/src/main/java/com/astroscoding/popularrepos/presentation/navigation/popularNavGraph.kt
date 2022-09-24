package com.astroscoding.popularrepos.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.astroscoding.common.domain.model.Repo
import com.astroscoding.common.presentation.Destination
import com.astroscoding.popularrepos.presentation.PopularReposViewModel
import com.astroscoding.popularrepos.presentation.comp.PopularReposComposable

fun NavGraphBuilder.popularNavGraph(
    popularReposViewModel: PopularReposViewModel,
    onRepoClicked: (repo: Repo) -> Unit
) {
    navigation(
        startDestination = Destination.PopularRepos.route,
        route = Destination.POPULAR_REPO_GRAPH
    ) {
        composable(Destination.PopularRepos.route) {
            PopularReposComposable(
                viewModel = popularReposViewModel,
                onRepoClicked = onRepoClicked
            )
        }
    }
}