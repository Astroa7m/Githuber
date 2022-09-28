package com.astroscoding.search.presentation.navigation

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.astroscoding.common.R.string.*
import com.astroscoding.common.domain.model.Repo
import com.astroscoding.common.presentation.Destination
import com.astroscoding.search.presentation.SearchReposViewModel
import com.astroscoding.search.presentation.comp.SearchRepoComposable

fun NavGraphBuilder.searchNavGraph(
    searchReposViewModel: SearchReposViewModel,
    context: Context,
    onRepoClicked: (repo: Repo) -> Unit
){
    navigation(
        startDestination = Destination.SearchRepos.route,
        route = Destination.SEARCH_REPO_GRAPH
    ) {
        composable(
            Destination.SearchRepos.route,
            deepLinks = listOf(
                navDeepLink {
                    val host = context.getString(host)
                    val scheme = context.getString(scheme)
                    val path = context.getString(path)
                    val uri = "$scheme://$host$path?repoName={repoName}&language={language}"
                    uriPattern = uri
                }
            )
        ) { backstackEntry ->
            val queryFromDeepLink = backstackEntry.arguments?.getString("repoName")
            val language = backstackEntry.arguments?.getString("language")
            SearchRepoComposable(
                viewModel = searchReposViewModel,
                onRepoClicked = onRepoClicked,
                queryFromDeepLink = queryFromDeepLink,
                language = language
            )
        }
    }
}