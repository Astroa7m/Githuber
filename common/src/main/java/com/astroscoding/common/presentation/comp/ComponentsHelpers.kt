package com.astroscoding.common.presentation.comp

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color


private const val DEFAULT_COLOR = "41b883"
val languages = listOf(
    "kotlin",
    "java",
    "c#",
    "rust",
    "python",
    "go" ,
    "scala",
    "powershell",
    "html",
    "c++",
    "typescript",
    "matlab",
    "haxe",
    "jupyter notebook",
    "c",
    "css",
    "awk",
    "zip",
    "clojure",
    "php",
    "objective-c",
    "vue",
    "dart",
    "ruby",
    "jinja",
    "shell",
    "groovy",
    "vim script",
    "scss",
    "swift",
)
private val colors = listOf(
     "A97BFF",
     "b07219",
     "178600",
     "dea584",
     "3572A5",
     "00ADD8",
     "c22d40",
     "012456",
     "e34c26",
    "f34b7d",
     "3178c6",
    "e16737",
    "df7900",
    "DA5B0B",
    "555555",
    "563d7c",
    "c30e9b",
    "ec915c",
    "db5855",
    "4F5D95",
    "438eff",
    "41b883",
    "00B4AB",
    "701516",
     "a52a22",
    "89e051",
    "4298b8",
    "199f4b",
    "c6538c",
     "F05138"
)
private val languagesColors = languages.mapIndexed{ index, lang ->
    lang to colors[index]
}.toMap()

fun getLanguageColor(language: String): Color {
    val color = if (language == "any")
        "#f3f6f4"
    else
        "#${languagesColors.get(language.lowercase()) ?: DEFAULT_COLOR}"
    return Color(android.graphics.Color.parseColor(color))
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

