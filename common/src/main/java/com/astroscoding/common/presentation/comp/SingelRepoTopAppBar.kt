package com.astroscoding.common.presentation.comp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.astroscoding.common.domain.model.Repo

@Composable
fun SingleRepoTopAppBar(
    modifier: Modifier = Modifier,
    repo: Repo,
    onShareClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Repository Details",
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = repo.owner.avatarUrl,
                    contentDescription = "${repo.owner.username}'s profile url",
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape),

                    )
            }
        },
        actions = {
            IconButton(onClick = onShareClicked) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share repo")
            }
        }
    )
}