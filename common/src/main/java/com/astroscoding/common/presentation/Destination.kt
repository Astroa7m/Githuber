package com.astroscoding.common.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String, val label: String, val imageVector: ImageVector) {
    companion object{
        const val ROOT_GRAPH = "root_graph"
        const val POPULAR_REPO_GRAPH = "popular_repos_graph"
        const val SEARCH_REPO_GRAPH = "search_repos_graph"
    }
    object PopularRepos: Destination("popular_repos", "Repos", Icons.Default.Home)
    object SearchRepos: Destination("search_repos", "Search", Icons.Default.Search)
}