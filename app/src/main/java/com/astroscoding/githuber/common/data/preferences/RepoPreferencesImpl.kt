package com.astroscoding.githuber.common.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoPreferencesImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
) : RepoPreferences {

    override val language: Flow<String> = datastore.data
        .map { pref ->
            pref[LANGUAGE_PREF_KEY] ?: Constants.DEFAULT_LANGUAGE
        }

    override suspend fun changeLanguage(language: String) {
        datastore.edit { pref ->
            pref[LANGUAGE_PREF_KEY] = language
        }
    }

    override val sortOrder: Flow<Sort> = datastore.data
        .map { pref ->
            val sort = pref[SORT_ORDER_PREF_KEY] ?: Constants.DEFAULT_SORT_TYPE.sort
            when(sort){
                Sort.Forks.sort -> Sort.Forks
                Sort.Issues.sort -> Sort.Issues
                else -> Sort.Stars
            }
        }


    override suspend fun changeSortOrder(sort: Sort) {
        datastore.edit { pref ->
            pref[SORT_ORDER_PREF_KEY] = sort.sort
        }
    }

    companion object {
        private val LANGUAGE_PREF_KEY = stringPreferencesKey(PreferencesConstants.LANGUAGE_KEY)
        private val SORT_ORDER_PREF_KEY = stringPreferencesKey(PreferencesConstants.SORT_ORDER_KEY)
    }

}