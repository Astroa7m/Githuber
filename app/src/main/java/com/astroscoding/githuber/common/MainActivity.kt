package com.astroscoding.githuber.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.astroscoding.githuber.common.presentation.comp.DefaultAppBar
import com.astroscoding.githuber.common.presentation.comp.NavController
import com.astroscoding.githuber.common.presentation.comp.ReposBottomNavigationBar
import com.astroscoding.githuber.common.presentation.ui.theme.GithuberTheme
import com.astroscoding.githuber.common.util.languageCharToLanguageSymbol
import com.astroscoding.githuber.popularrepos.presentation.PopularReposUIEvent
import com.astroscoding.githuber.popularrepos.presentation.PopularReposViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val popularReposViewModel: PopularReposViewModel = hiltViewModel()
            GithuberTheme {
                Scaffold(
                    bottomBar = {
                        ReposBottomNavigationBar(navController = navController)
                    },
                    topBar = {
                        DefaultAppBar(
                            currentSort = popularReposViewModel.sort,
                            onNewSort = {
                                popularReposViewModel.onEvent(
                                    PopularReposUIEvent.SelectNewSort(it)
                                )
                            },
                            currentLanguage = languageCharToLanguageSymbol(popularReposViewModel.lang),
                            onLanguageSelected = {
                                popularReposViewModel.onEvent(
                                    PopularReposUIEvent.SelectNewLanguage(it)
                                )
                            }
                        )
                    }
                ) { paddingValues ->
                    NavController(
                        navController = navController,
                        paddingValues = paddingValues,
                        popularReposViewModel = popularReposViewModel
                    )
                }
            }
        }
    }
}
