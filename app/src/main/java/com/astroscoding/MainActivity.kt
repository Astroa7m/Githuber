package com.astroscoding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.astroscoding.common.presentation.Destination
import com.astroscoding.common.presentation.SingleRepoViewModel
import com.astroscoding.common.presentation.comp.DefaultAppBar
import com.astroscoding.common.presentation.comp.ReposBottomNavigationBar
import com.astroscoding.common.presentation.comp.SingleRepoTopAppBar
import com.astroscoding.common.presentation.ui.theme.GithuberTheme
import com.astroscoding.common.util.languageCharToLanguageSymbol
import com.astroscoding.navigation.GitHuberNavHost
import com.astroscoding.popularrepos.presentation.PopularReposViewModel
import com.astroscoding.search.presentation.SearchReposViewModel
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
            val singleRepoViewModel: SingleRepoViewModel = hiltViewModel()
            searchReposViewModel.searchScreenIsCurrentlyOpen.value = currentDestination == Destination.SearchRepos.route

            GithuberTheme {
                Scaffold(
                    bottomBar = {
                        if (currentDestination == Destination.PopularRepos.route || currentDestination == Destination.SearchRepos.route){
                            ReposBottomNavigationBar(navController = navController)
                        }
                    },
                    topBar = {
                        if (currentDestination == Destination.PopularRepos.route) {
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
                        } else if (currentDestination == Destination.SearchRepos.route){
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
                        } else{
                            singleRepoViewModel.repo.value?.let {
                                SingleRepoTopAppBar(repo = it){

                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        GitHuberNavHost(
                            navController = navController,
                            popularReposViewModel = popularReposViewModel,
                            searchReposViewModel = searchReposViewModel,
                            singleRepoViewModel = singleRepoViewModel
                        )
                    }
                }
            }
        }
    }
}
