package com.astroscoding.githuber.common.data.di

import com.astroscoding.githuber.common.data.RepositoriesRepositoryImpl
import com.astroscoding.githuber.common.domain.repository.RepositoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBinding {

    @Binds
    abstract fun bindRepository(repository: RepositoriesRepositoryImpl) : RepositoriesRepository
}