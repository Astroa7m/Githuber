package com.astroscoding.githuber.common.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String, val label: String, val imageVector: ImageVector) {
    object PopularRepos: Destination("popular_repos", "Repos", Icons.Default.Home)
    object SearchRepos: Destination("search_repos", "Search", Icons.Default.Search)
}