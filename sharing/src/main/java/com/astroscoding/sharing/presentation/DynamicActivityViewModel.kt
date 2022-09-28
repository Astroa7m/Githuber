package com.astroscoding.sharing.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.astroscoding.common.data.preferences.RepoPreferences
import com.astroscoding.common.domain.model.Repo
import javax.inject.Inject

class DynamicActivityViewModel @Inject constructor(
    repoPreferences: RepoPreferences
) : ViewModel() {

    val repo = mutableStateOf<Repo?>(null)
    val currentLanguage = repoPreferences.language

}