package com.astroscoding.githuber.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.astroscoding.githuber.common.presentation.ui.theme.GithuberTheme
import com.astroscoding.githuber.common.presentation.PopularReposComposable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithuberTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PopularReposComposable(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
