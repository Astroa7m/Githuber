package com.astroscoding.sharing.di

import android.content.Context
import com.astroscoding.di.SharingModuleDependencies
import com.astroscoding.sharing.presentation.DynamicActivity
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [SharingModuleDependencies::class],
    modules = [SharingModule::class]
)

interface SharingComponent {

    fun inject(activity: DynamicActivity)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun moduleDependencies(sharingModuleDependencies: SharingModuleDependencies): Builder
        fun build(): SharingComponent
    }
}