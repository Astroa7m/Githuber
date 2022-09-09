package com.astroscoding.githuber.common.presentation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue


private const val DEFAULT_COLOR = "41b883"

private val languagesColors = mapOf(
    "kotlin" to "A97BFF",
    "java" to "b07219",
    "c#" to "178600",
    "rust" to "dea584",
    "python" to "3572A5",
    "go" to "00ADD8",
    "scala" to "c22d40",
    "powershell" to "012456",
    "html" to "e34c26",
    "c++" to "f34b7d",
    "typescript" to "3178c6",
    "matlap" to "e16737",
    "haxe" to "df7900",
    "jupyter notebook" to "DA5B0B",
    "c" to "555555",
    "css" to "563d7c",
    "awk" to "c30e9b",
    "zip" to "ec915c",
    "clojure" to "db5855",
    "php" to "4F5D95",
    "objective-c" to "438eff",
    "vue" to "41b883",
    "dart" to "00B4AB",
    "ruby" to "701516",
    "jinja" to "a52a22",
    "shell" to "89e051",
    "groovy" to "4298b8",
    "vim script" to "199f4b",
    "scss" to "c6538c",
    "swift" to "F05138"
)

fun getLanguageColor(language: String): Color {
    return Color(android.graphics.Color.parseColor("#${languagesColors.get(language.lowercase()) ?: DEFAULT_COLOR}"))
}

data class ScrollContext(
    val isTop: Boolean,
    val isBottom: Boolean,
)

val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

val LazyListState.isFirstItemVisible: Boolean
    get() = firstVisibleItemIndex == 0

@Composable
fun rememberScrollContext(listState: LazyListState): ScrollContext {
    val scrollContext by remember {
        derivedStateOf {
            ScrollContext(
                isTop = listState.isFirstItemVisible,
                isBottom = listState.isLastItemVisible
            )
        }
    }
    return scrollContext
}

