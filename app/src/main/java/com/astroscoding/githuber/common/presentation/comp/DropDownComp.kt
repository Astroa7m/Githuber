package com.astroscoding.githuber.common.presentation.comp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.astroscoding.githuber.R
import com.astroscoding.githuber.common.domain.model.Sort
import java.util.*

@Composable
fun SortingDropDown(
    modifier: Modifier = Modifier,
    currentSort: Sort,
    onNewSort: (Sort) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.Center) {
        IconButton(
            modifier = modifier,
            onClick = { expanded = true }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sort),
                contentDescription = "filter"
            )
        }

        SortingMenu(
            expanded = expanded,
            currentSort = currentSort,
            onDismissed = { expanded = false },
            onSortSelected = onNewSort
        )

    }
}

@Composable
fun SortingMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    currentSort: Sort,
    onSortSelected: (Sort) -> Unit,
    onDismissed: () -> Unit
) {
    var sortOrder by remember { mutableStateOf(currentSort) }
    val sortList = remember {
        listOf(
            Sort.Stars,
            Sort.Forks,
            Sort.Issues
        )
    }
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = {
            onDismissed()
        }
    ) {

        sortList.forEach { sort ->
            DropdownMenuItem(
                text = {
                    Text(text = sort.sort.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    })
                },
                onClick = {
                    sortOrder = sort
                    onSortSelected(sortOrder)
                },
                leadingIcon = {
                    when (sort) {
                        Sort.Forks -> Icon(
                            painter = painterResource(R.drawable.fork_ic),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Sort.Issues -> Icon(
                            painter = painterResource(id = R.drawable.issue_ic),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Sort.Stars -> Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)

                        )
                        else -> {}
                    }
                },
                trailingIcon = {
                    if (currentSort == sort) {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                    }
                }
            )
        }
    }
}

@Composable
fun LanguageDropDown(
    modifier: Modifier = Modifier,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.Center) {
        IconButton(
            modifier = modifier,
            onClick = { expanded = true }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_language),
                contentDescription = "filter"
            )
        }
        LanguageMenu(
            expanded = expanded,
            currentLanguage = currentLanguage,
            onLanguageSelected = onLanguageSelected,
            onDismissed = { expanded = false }
        )

    }
}

@Composable
fun LanguageMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismissed: () -> Unit
) {
    var language by remember { mutableStateOf(currentLanguage) }
    var launchTypeLanguageDialog by remember { mutableStateOf(false) }
    var createCustomLanguageItem by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = currentLanguage){
        createCustomLanguageItem = currentLanguage.lowercase() !in languages
    }

    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissed
    ) {

        if (createCustomLanguageItem) {
            DropdownMenuItem(text = {
                Text(text = currentLanguage.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                })
            }, onClick = {},
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(getLanguageColor(currentLanguage))
                    )
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                }

            )
        }

        languages.forEach {

            DropdownMenuItem(text = {
                Text(text = it.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                })
            }, onClick = {
                if (currentLanguage != it){
                    language = it
                    onLanguageSelected(language)
                }
            },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(getLanguageColor(it))

                    )
                },
                trailingIcon = {
                    if (currentLanguage == it) {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                    }
                }
            )
        }
        Divider()
        DropdownMenuItem(
            text = { Text(text = "None of these?") },
            onClick = {
                launchTypeLanguageDialog = true
                onDismissed()
            },
            trailingIcon = {
                Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null)
            }
        )
    }
    if (launchTypeLanguageDialog)
        TypeLanguageDialog(
            onDismissed = { launchTypeLanguageDialog = false },
            onConfirm = {
                language = it
                onLanguageSelected(language)
            }
        )

}

@Composable
fun TypeLanguageDialog(
    modifier: Modifier = Modifier,
    onDismissed: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val context = LocalContext.current
    val (customLanguage, onCustomLanguageChanged) = remember { mutableStateOf("") }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissed,
        title = { Text(text = "Didn't find your language?") },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(customLanguage)
                    onDismissed()
                    Toast.makeText(
                        context,
                        "Language is set to $customLanguage",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                enabled = customLanguage.isNotEmpty()
            ) {
                Text(text = "Confirm")
            }
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_language),
                contentDescription = "filter"
            )
        },
        text = {
            TextField(
                value = customLanguage,
                onValueChange = onCustomLanguageChanged,
                label = {
                    Text(text = "Type your language")
                }
            )
        }
    )
}



