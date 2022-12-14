package com.astroscoding.common.data.di

import com.astroscoding.common.data.preferences.RepoPreferences
import com.astroscoding.common.data.preferences.RepoPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesBinding {

    @Binds
    abstract fun bindPref(preferencesImpl: RepoPreferencesImpl) : RepoPreferences
}