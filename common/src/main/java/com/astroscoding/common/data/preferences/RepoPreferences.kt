package com.astroscoding.common.data.preferences

import com.astroscoding.common.domain.model.Sort
import kotlinx.coroutines.flow.Flow


interface RepoPreferences {
    val language: Flow<String>
    val sortOrder: Flow<Sort>
    suspend fun changeLanguage(language: String)
    suspend fun changeSortOrder(sort: Sort)
}