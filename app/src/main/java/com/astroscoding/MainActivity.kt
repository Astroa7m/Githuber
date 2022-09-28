package com.astroscoding

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.astroscoding.common.presentation.Destination
import com.astroscoding.common.presentation.SingleRepoViewModel
import com.astroscoding.common.presentation.comp.DefaultAppBar
import com.astroscoding.common.presentation.comp.ReposBottomNavigationBar
import com.astroscoding.common.presentation.comp.SingleRepoTopAppBar
import com.astroscoding.common.presentation.ui.theme.GithuberTheme
import com.astroscoding.common.util.languageCharToLanguageSymbol
import com.astroscoding.comp.CheckDynamicFeatureStatus
import com.astroscoding.navigation.GitHuberNavHost
import com.astroscoding.popularrepos.presentation.PopularReposViewModel
import com.astroscoding.search.presentation.SearchReposViewModel
import com.google.android.play.core.splitinstall.SplitInstallHelper
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val FEATURE_MODULE = "sharing"

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    lateinit var splitManager: SplitInstallManager

    @Inject
    lateinit var mainViewModel: MainActivityViewModel

    @Inject
    lateinit var actions: Actions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splitManager = SplitInstallManagerFactory.create(this)
        setContent {
            GithuberTheme {
                App()
            }
        }
    }

    @Composable
    private fun App(
        navController: NavHostController = rememberNavController(),
        currentDestination: String? = navController.currentBackStackEntryAsState().value?.destination?.route,
        popularReposViewModel: PopularReposViewModel = hiltViewModel(),
        searchReposViewModel: SearchReposViewModel = hiltViewModel(),
        singleRepoViewModel: SingleRepoViewModel = hiltViewModel(),
    ) {
        searchReposViewModel.searchScreenIsCurrentlyOpen.value =
            currentDestination == Destination.SearchRepos.route

        Scaffold(
            bottomBar = {
                if (currentDestination == Destination.PopularRepos.route || currentDestination == Destination.SearchRepos.route) {
                    ReposBottomNavigationBar(navController = navController)
                }
            },
            topBar = {
                when (currentDestination) {
                    Destination.PopularRepos.route -> {
                        DefaultAppBar(
                            currentSort = popularReposViewModel.sort.collectAsState().value,
                            onNewSort = {
                                popularReposViewModel.onEvent(
                                    com.astroscoding.popularrepos.presentation.PopularReposUIEvent.SelectNewSort(
                                        it
                                    )
                                )
                            },
                            currentLanguage = languageCharToLanguageSymbol(
                                popularReposViewModel.language.collectAsState().value
                            ),
                            onLanguageSelected = {
                                popularReposViewModel.onEvent(
                                    com.astroscoding.popularrepos.presentation.PopularReposUIEvent.SelectNewLanguage(
                                        it
                                    )
                                )
                            }
                        )
                    }
                    Destination.SearchRepos.route -> {
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
                    else -> {
                        singleRepoViewModel.repo.value?.let {
                            SingleRepoTopAppBar(repo = it) {
                                mainViewModel.repo = it
                                checkDynamicFeature()
                            }
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

                CheckDynamicFeatureStatus(mainViewModel) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // Updates the app’s context with the code and resources of the
                        // installed module.
                        SplitInstallHelper.updateAppInfo(this@MainActivity)
                    }
                    navigateToDynamicFeature(mainViewModel.repo)
                }

            }
        }
    }

    override fun onResume() {
        splitManager.registerListener(actions.listener)
        super.onResume()
    }

    override fun onPause() {
        splitManager.unregisterListener(actions.listener)
        super.onPause()
    }
}
