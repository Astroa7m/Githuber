package com.astroscoding.common.presentation.comp

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.astroscoding.common.presentation.Destination

@Composable
fun ReposBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val menuItem = listOf(
        Destination.PopularRepos,
        Destination.SearchRepos
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(modifier = modifier) {
        menuItem.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    // if navigating from current destination to the current destination
                    // then restore the state by popping the backstack (removing old screen)
                    // and navigating to current destination (launching new screen)
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            // always save the state of the current destination
                            // if we are navigating to any other destination
                            saveState = true
                        }
                        // restore the saved state once we pop the current screen from
                        // the backstack
                        restoreState = true
                    }
                },
                label = {
                    Text(text = screen.label)
                },
                icon = {
                    Icon(imageVector = screen.imageVector, contentDescription = screen.route)
                }
            )

        }
    }

}