package com.astroscoding.githuber.common.data.di

import com.astroscoding.githuber.common.data.PopularRepositoriesRepositoryImpl
import com.astroscoding.githuber.common.domain.repository.PopularRepositoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBinding {

    @Binds
    abstract fun bindRepository(repository: PopularRepositoriesRepositoryImpl) : PopularRepositoriesRepository
}