package com.astroscoding.sharing.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.astroscoding.sharing.presentation.DynamicActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoMap

@Module
@DisableInstallInCheck
abstract class SharingModule {

    @Binds
    @IntoMap
    @ViewModelKey(DynamicActivityViewModel::class)
    abstract fun bindDynamicActivityViewModel(
        dynamicActivityViewModel: DynamicActivityViewModel
    ): ViewModel

    @Binds
    @Reusable
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}