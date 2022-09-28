package com.astroscoding

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.astroscoding.common.domain.model.Repo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityViewModel @Inject constructor(): ViewModel()  {
    val loading = mutableStateOf(false)
    val featureInstalled = mutableStateOf(false)
    val failureOccurred = mutableStateOf(false)
    var repo: Repo? = null
}