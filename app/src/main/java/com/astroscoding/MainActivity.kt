package com.astroscoding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.astroscoding.common.presentation.Destination
import com.astroscoding.common.presentation.comp.DefaultAppBar
import com.astroscoding.common.presentation.comp.ReposBottomNavigationBar
import com.astroscoding.common.presentation.ui.theme.GithuberTheme
import com.astroscoding.common.util.languageCharToLanguageSymbol
import com.astroscoding.popularrepos.presentation.PopularReposViewModel
import com.astroscoding.search.presentation.SearchReposViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: remove unnecessary dependencies in build.gradle(app)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentDestination =
                navController.currentBackStackEntryAsState().value?.destination?.route
            val popularReposViewModel: PopularReposViewModel = hiltViewModel()
            val searchReposViewModel: SearchReposViewModel = hiltViewModel()
            searchReposViewModel.searchScreenIsCurrentlyOpen.value = currentDestination == Destination.SearchRepos.route

            GithuberTheme {
                Scaffold(
                    bottomBar = {
                        ReposBottomNavigationBar(navController = navController)
                    },
                    topBar = {
                        if (!searchReposViewModel.searchScreenIsCurrentlyOpen.value) {
                            DefaultAppBar(
                                currentSort = popularReposViewModel.sort.collectAsState().value,
                                onNewSort = {
                                    popularReposViewModel.onEvent(
                                        com.astroscoding.popularrepos.presentation.PopularReposUIEvent.SelectNewSort(it)
                                    )
                                },
                                currentLanguage = languageCharToLanguageSymbol(popularReposViewModel.language.collectAsState().value),
                                onLanguageSelected = {
                                    popularReposViewModel.onEvent(
                                        com.astroscoding.popularrepos.presentation.PopularReposUIEvent.SelectNewLanguage(it)
                                    )
                                }
                            )
                        } else {
                            com.astroscoding.search.presentation.comp.SearchTopAppBar(
                                queryText = searchReposViewModel.searchQuery.collectAsState().value,
                                onLanguageSelected = {
                                    searchReposViewModel.onEvent(
                                        com.astroscoding.search.presentation.SearchReposUIEvent.SelectNewLanguage(
                                            it
                                        )
                                    )
                                },
                                onNewSort = {
                                    searchReposViewModel.onEvent(
                                        com.astroscoding.search.presentation.SearchReposUIEvent.SelectNewSort(
                                            it
                                        )
                                    )
                                },
                                onCloseIconClicked = { searchReposViewModel.onEvent(com.astroscoding.search.presentation.SearchReposUIEvent.CloseSearch) },
                                onQueryTextChange = {
                                    searchReposViewModel.onEvent(
                                        com.astroscoding.search.presentation.SearchReposUIEvent.InputQuery(
                                            it
                                        )
                                    )
                                },
                                onSearchIconClicked = {
                                    searchReposViewModel.onEvent(com.astroscoding.search.presentation.SearchReposUIEvent.SearchRepos)
                                },
                                currentLanguage = searchReposViewModel.language.collectAsState().value,
                                currentSort = searchReposViewModel.sort.collectAsState().value
                            )
                        }
                    }
                ) { paddingValues ->
                    GitHuberNavHost(
                        navController = navController,
                        paddingValues = paddingValues,
                        popularReposViewModel = popularReposViewModel,
                        searchReposViewModel = searchReposViewModel
                    )
                }
            }
        }
    }
}
