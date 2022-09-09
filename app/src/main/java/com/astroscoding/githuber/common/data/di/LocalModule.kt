package com.astroscoding.githuber.common.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.astroscoding.githuber.common.data.local.RepositoryDao
import com.astroscoding.githuber.common.data.local.RepositoryDatabase
import com.astroscoding.githuber.common.data.preferences.PreferencesConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RepositoryDatabase {
        return Room.databaseBuilder(
            context,
            RepositoryDatabase::class.java,
            "repo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatastorePreferences(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = scope
        ) {
            context.preferencesDataStoreFile(PreferencesConstants.PREFERENCES_NAME)
        }
    }

}

