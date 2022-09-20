package com.astroscoding.common.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.astroscoding.githuber.common.data.local.RepositoryDatabase
import com.astroscoding.githuber.common.data.remote.GithubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import kotlin.random.Random

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [LocalModule::class, RemoteModule::class])
object TestAppModule {

    @Singleton
    @Provides
    fun provideRepoDatabaseInMemory(
        @ApplicationContext context: Context
    ): RepositoryDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            RepositoryDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideMockWebServer(): MockWebServer = MockWebServer()

    @Provides
    @Singleton
    fun provideFakeApi(mockWebServer: MockWebServer): GithubApi {
        mockWebServer.start(8080)
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }


    @Singleton
    @Provides
    fun provideFakePreferences(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): DataStore<Preferences> {
        val random = Random.nextInt()
        return PreferenceDataStoreFactory
            .create(
                scope = scope,
                produceFile = {
                    context.preferencesDataStoreFile("test_pref_file-$random")
                }
            )
    }

}