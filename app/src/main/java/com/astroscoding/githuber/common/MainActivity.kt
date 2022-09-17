package com.astroscoding.githuber.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.presentation.Destination
import com.astroscoding.githuber.common.presentation.comp.DefaultAppBar
import com.astroscoding.githuber.common.presentation.comp.NavController
import com.astroscoding.githuber.common.presentation.comp.ReposBottomNavigationBar
import com.astroscoding.githuber.common.presentation.ui.theme.GithuberTheme
import com.astroscoding.githuber.common.util.languageCharToLanguageSymbol
import com.astroscoding.githuber.popularrepos.presentation.PopularReposUIEvent
import com.astroscoding.githuber.popularrepos.presentation.PopularReposViewModel
import com.astroscoding.githuber.search.presentation.SearchReposUIEvent
import com.astroscoding.githuber.search.presentation.SearchReposViewModel
import com.astroscoding.githuber.search.presentation.comp.SearchStart
import com.astroscoding.githuber.search.presentation.comp.SearchTopAppBar
import dagger.hilt.android.AndroidEntryPoint

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
                                        PopularReposUIEvent.SelectNewSort(it)
                                    )
                                },
                                currentLanguage = languageCharToLanguageSymbol(popularReposViewModel.language.collectAsState().value),
                                onLanguageSelected = {
                                    popularReposViewModel.onEvent(
                                        PopularReposUIEvent.SelectNewLanguage(it)
                                    )
                                }
                            )
                        } else {
                            SearchTopAppBar(
                                queryText = searchReposViewModel.searchQuery.collectAsState().value,
                                onLanguageSelected = {
                                    searchReposViewModel.onEvent(
                                        SearchReposUIEvent.SelectNewLanguage(
                                            it
                                        )
                                    )
                                },
                                onNewSort = {
                                    searchReposViewModel.onEvent(SearchReposUIEvent.SelectNewSort(it))
                                },
                                onCloseIconClicked = { searchReposViewModel.onEvent(SearchReposUIEvent.CloseSearch) },
                                onQueryTextChange = {
                                    searchReposViewModel.onEvent(SearchReposUIEvent.InputQuery(it))
                                },
                                onSearchIconClicked = {
                                    searchReposViewModel.onEvent(SearchReposUIEvent.SearchRepos)
                                },
                                currentLanguage = searchReposViewModel.language.collectAsState().value,
                                currentSort = searchReposViewModel.sort.collectAsState().value
                            )
                        }
                    }
                ) { paddingValues ->
                    NavController(
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
