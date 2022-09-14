package com.astroscoding.githuber.common.presentation.comp

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.astroscoding.githuber.R
import com.astroscoding.githuber.common.domain.model.Repo
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReposComposable(
    modifier: Modifier = Modifier,
    repos: List<Repo>,
    loading: Boolean,
    errorMessage: String,
    onRefresh: (() -> Unit)? = null,
    onLastItemReached: () -> Unit,
    animate: Boolean,
    onDoneAnimating: () -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = loading)
    val lazyListState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = lazyListState)

    LaunchedEffect(key1 = scrollContext.isBottom) {
        if (scrollContext.isBottom && repos.isNotEmpty())
            onLastItemReached()
    }

    LaunchedEffect(key1 = animate){
        // some delay so we receive the freshly sorted list before scrolling
        delay(500)
        if (animate){
            launch {
                lazyListState.scrollToItem(0)
            }.join()
            onDoneAnimating()
        }
    }

    SwipeRefresh(
        modifier = modifier,
        state = swipeRefreshState,
        swipeEnabled = onRefresh != null,
        onRefresh = { onRefresh?.let { it() } }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (errorMessage.isNotEmpty())
                Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
            AnimatedVisibility(
                visible = repos.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                RepoPlaceHolder(Modifier.fillMaxWidth())
            }
            AnimatedVisibility(
                visible = repos.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    state = lazyListState
                ) {
                    items(
                        items = repos,
                        key = {repo-> repo.id}
                    ) { repo ->
                        PopularRepoItems(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            repo = repo,
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun RepoPlaceHolder(modifier: Modifier = Modifier) {
    Column(modifier) {
        repeat(5) {
            RepoPlaceHolderItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularRepoItems(
    modifier: Modifier = Modifier,
    repo: Repo
) {
    Card(modifier) {
        RepoNameAndDesc(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 16.dp, 8.dp, 8.dp),
            name = repo.name,
            description = repo.description
        )
        RepoCountsDetails(repo = repo, modifier = Modifier.padding(8.dp))
        LanguageText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            language = repo.language
        )
        Tags(
            repo = repo,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp, 8.dp, 8.dp, 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoPlaceHolderItem(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.surfaceVariant,
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    Card(modifier.height(250.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
        )
    }
}

@Composable
fun Tags(
    modifier: Modifier = Modifier,
    repo: Repo
) {
    FlowRow(
        modifier = modifier,
        mainAxisAlignment = FlowMainAxisAlignment.Center,
        crossAxisAlignment = FlowCrossAxisAlignment.Center,
        crossAxisSpacing = 8.dp
    ) {
        if (repo.topics.isNotEmpty()) {
            repo.topics.forEachIndexed { index, topic ->
                if (topic.isNotEmpty()) {
                    Text(
                        text = "#$topic",
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

@Composable
fun LanguageText(
    modifier: Modifier = Modifier,
    language: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(getLanguageColor(language))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = language,
            modifier = modifier
        )
    }
}

@Composable
fun RepoNameAndDesc(modifier: Modifier = Modifier, name: String, description: String) {
    val (expanded, onExpanded) = rememberSaveable { mutableStateOf(false) }
    var hasOverflow by remember { mutableStateOf(false) }
    var cutString by remember { mutableStateOf<String?>(null) }
    val seeMore = " See more"
    val maxTextLines = 1
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //repo name

        Text(
            text = name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        val descriptionText = buildAnnotatedString {
            if (hasOverflow && expanded.not()) {
                cutString?.let {
                    append(it)
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(seeMore)
                }
            }
            append(description)

        }
        //repo description
        Text(
            text = descriptionText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = if (expanded) Int.MAX_VALUE else maxTextLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .clickable(enabled = hasOverflow) { onExpanded(true) }
                .animateContentSize(),
            onTextLayout = { textLayoutResult ->
                hasOverflow = textLayoutResult.hasVisualOverflow
                val lastCharIndex = textLayoutResult.getLineEnd(maxTextLines - 1, true)
                cutString = descriptionText.substring(0, lastCharIndex)
                    .dropLast(seeMore.length)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoCountsDetails(
    modifier: Modifier = Modifier,
    repo: Repo,
) {

    FlowRow(
        modifier = modifier.fillMaxSize(),
        mainAxisAlignment = FlowMainAxisAlignment.Center,
        crossAxisAlignment = FlowCrossAxisAlignment.Center,
        crossAxisSpacing = 8.dp
    ) {
        //stars
        AssistChip(
            onClick = {},
            label = { Text(text = repo.starsCount.toString()) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Repo stars",
                    modifier = Modifier.size(25.dp)
                )
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        //forks
        AssistChip(
            onClick = {},
            label = { Text(text = repo.forksCount.toString()) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.fork_ic),
                    contentDescription = "Repo stars",
                    modifier = Modifier.size(25.dp)
                )
            }
        )
        Spacer(modifier = Modifier.width(8.dp))

        //issues
        AssistChip(
            onClick = {},
            label = { Text(text = repo.issuesCount.toString()) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.issue_ic),
                    contentDescription = "Repo stars",
                    modifier = Modifier.size(25.dp),
                )
            }
        )
        Spacer(modifier = Modifier.width(8.dp))

        //license
        AssistChip(
            onClick = {},
            label = { Text(text = repo.licenseName) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_license),
                    contentDescription = "Repo stars",
                    modifier = Modifier.size(25.dp)
                )
            }
        )
    }
}

@Composable
fun TextedIcon(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit),
    icon: @Composable (() -> Unit)
) {
    Box(
        modifier
            .border(
                BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary
                ), MaterialTheme.shapes.extraLarge
            )
            .padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            label()
            icon()
        }
    }
}
