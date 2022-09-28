package com.astroscoding.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SharingModuleDependencies{
    // TODO: add needed deps here and in sharing:SharingComponent
}