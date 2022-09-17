package com.astroscoding.githuber.search.presentation.comp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.presentation.Destination
import com.astroscoding.githuber.common.presentation.comp.LanguageDropDown
import com.astroscoding.githuber.common.presentation.comp.SortingDropDown

@Composable
fun SearchTopAppBar(
    modifier: Modifier = Modifier,
    placeholderText: String = "Username, repository, file... ",
    queryText: String,
    onQueryTextChange: (String) -> Unit,
    onCloseIconClicked: () -> Unit,
    onSearchIconClicked: () -> Unit,
    currentSort: Sort,
    onNewSort: (Sort) -> Unit,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),/*smallTopAppBar default height*/
        shadowElevation = 3.0.dp /*Default shadow for smallTopAppBar*/
    ) {
        Column {
            TextField(
                modifier = Modifier
                    .fillMaxSize(),
                value = queryText,
                onValueChange = onQueryTextChange,
                placeholder = {
                    Text(
                        text = placeholderText,
                        Modifier.alpha(0.6f)
                    )
                },
                leadingIcon = {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        onSearchIconClicked()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search icon"
                        )
                    }
                },
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = {
                            focusManager.clearFocus()
                            onCloseIconClicked()
                        }, enabled = queryText.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "close icon"
                            )
                        }
                        LanguageDropDown(
                            currentLanguage = currentLanguage,
                            onLanguageSelected = onLanguageSelected,
                            currentScreen = Destination.SearchRepos
                        )
                        SortingDropDown(
                            currentSort = currentSort,
                            onNewSort = onNewSort
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchIconClicked()
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                )
            )
            // maybe later when I extract it into a separate library
            /*var expanded by remember(queryText) {
                mutableStateOf(queryText.isNotEmpty())
            }

            InTextFilterDropDown(
                currentQuery = queryText,
                expanded = expanded,
                onDismissed = { expanded = false },
                modifier = Modifier.offset(48.dp),
                onFilterSelected = {
                    onFilterSelected(it)
                }
            )*/
        }
    }
}

/*@Composable
fun InTextFilterDropDown(
    modifier: Modifier = Modifier,
    currentQuery: String,
    expanded: Boolean,
    onDismissed: () -> Unit,
    onFilterSelected: (Filter) -> Unit
) {

    val searchBy = remember {
        listOf(
            Filter.Repository,
            Filter.User
        )
    }

    Box(modifier = modifier) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissed,
            properties = PopupProperties(focusable = false)
        ) {
            searchBy.forEach { searchBy ->
                DropdownMenuItem(
                    text = {
                        Text(text = "${searchBy.endpoint}: $currentQuery")
                    },
                    onClick = {
                        onFilterSelected(searchBy)
                    }
                )
            }
        }
    }
}*/
