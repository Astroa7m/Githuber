package com.astroscoding.common.presentation

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .scrollable(rememberScrollState(), Orientation.Vertical),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            repo?.let { repo ->
                RepoItem(
                    repo = repo
                )
            }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Hold click to navigate to the repository or the owner",
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
    }
}