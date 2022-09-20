package com.astroscoding.common.presentation.comp

import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.astroscoding.common.domain.model.Sort


@Composable
fun DefaultAppBar(
    modifier: Modifier = Modifier,
    titleText: String = "Popular Repositories",
    currentSort: Sort,
    onNewSort: (Sort) -> Unit,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    SmallTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = titleText,
                fontWeight = FontWeight.Medium
            )
        },
        actions = {
            LanguageDropDown(
                currentLanguage = currentLanguage,
                onLanguageSelected = onLanguageSelected
            )
            SortingDropDown(
                currentSort = currentSort,
                onNewSort = onNewSort
            )
        }
    )
}



