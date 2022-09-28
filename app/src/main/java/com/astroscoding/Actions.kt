package com.astroscoding

import android.util.Log
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Actions @Inject constructor(
    private val viewModel: MainActivityViewModel
) {

    val listener = SplitInstallStateUpdatedListener { state ->
        state.moduleNames().forEach { name ->
            // Handle changes in state.
            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    viewModel.loading.value = true
                    viewModel.featureInstalled.value = false
                    viewModel.failureOccurred.value = false
                    Log.d("SPLIT_TAG", ": DOWNLOADING")

                }
                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {

                }
                SplitInstallSessionStatus.INSTALLED -> {
                    Log.d("SPLIT_TAG", ": INSTALLED")
                    viewModel.featureInstalled.value = true
                    viewModel.failureOccurred.value = false
                    viewModel.loading.value = false

                }
                SplitInstallSessionStatus.FAILED -> {
                    Log.d("SPLIT_TAG", ": FAILED")
                    viewModel.failureOccurred.value = true
                    viewModel.loading.value = false
                    viewModel.featureInstalled.value = false

                }
                else -> {
                    Log.d("SPLIT_TAG", ": ELSE")
                }
            }

        }
    }

}