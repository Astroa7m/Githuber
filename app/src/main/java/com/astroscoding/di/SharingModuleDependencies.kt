package com.astroscoding.di

import com.astroscoding.common.data.preferences.RepoPreferences
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SharingModuleDependencies{
    fun repoPreferences(): RepoPreferences
}