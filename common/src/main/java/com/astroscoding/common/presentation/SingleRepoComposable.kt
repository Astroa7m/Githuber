package com.astroscoding.common.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.astroscoding.common.presentation.comp.RepoItem

@Composable
fun SingleRepoScreen(
    viewModel: SingleRepoViewModel
) {
    val repo by remember { viewModel.repo }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        repo?.let { repo ->
            RepoItem(
                repo = repo,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Text(
            text = "Hold click to navigate to the repository or the owner",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(
                    Alignment.BottomCenter
                )
                .padding(16.dp)
        )
    }
}