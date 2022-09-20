package com.astroscoding.common.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [CoroutineModule::class])
object CoroutineTestModule {

    @Singleton
    @Provides
    fun provideTestCoroutineScope(coroutineContext: CoroutineContext): CoroutineScope {
        return TestScope(coroutineContext)
    }

    @Singleton
    @Provides
    fun provideTestCoroutineContext(): CoroutineContext {
        return UnconfinedTestDispatcher() + Job()
    }
}