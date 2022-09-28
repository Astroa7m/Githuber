package com.astroscoding

import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import com.astroscoding.common.R
import com.astroscoding.common.domain.model.Repo
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode

fun MainActivity.navigateToDynamicFeature(repo: Repo?) {
    Intent(Intent.ACTION_VIEW).apply {
        val host = getString(R.string.host)
        val scheme = getString(R.string.scheme_in_app)
        val path = getString(R.string.path_in_app)
        data =
            "$scheme://$host$path".toUri()
        putExtra("repo", repo)
        `package` = this@navigateToDynamicFeature.packageName
        startActivity(this)
    }
}

fun MainActivity.checkDynamicFeature() {
    if (splitManager.installedModules.contains(FEATURE_MODULE)) {
        navigateToDynamicFeature(mainViewModel.repo)
        return
    }
    val request = SplitInstallRequest.newBuilder()
        .addModule(FEATURE_MODULE)
        .build()
    splitManager.startInstall(request).addOnFailureListener {
        when ((it as (SplitInstallException)).errorCode) {
            SplitInstallErrorCode.NETWORK_ERROR -> {
                // Display a message that requests the user to establish a
                // network connection.
                // If No Network loading Local Fragment
                Toast.makeText(this, "No Network Error", Toast.LENGTH_SHORT).show()
                mainViewModel.loading.value = false
            }
            else -> {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                mainViewModel.loading.value = false
            }
        }
    }
}
