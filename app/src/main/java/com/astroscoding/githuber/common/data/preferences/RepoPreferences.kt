package com.astroscoding.githuber.common.data.preferences

import com.astroscoding.githuber.common.domain.model.Sort
import kotlinx.coroutines.flow.Flow


interface RepoPreferences {
    val language: Flow<String>
    val sortOrder: Flow<Sort>
    suspend fun changeLanguage(language: String)
    suspend fun changeSortOrder(sort: Sort)
}