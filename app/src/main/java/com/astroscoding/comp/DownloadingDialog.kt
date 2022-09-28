package com.astroscoding.comp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.astroscoding.MainActivity
import com.astroscoding.MainActivityViewModel

@Composable
fun DownloadingFeatureComposable() {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Feature is being downloaded, please wait",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}

@Composable
fun MainActivity.CheckDynamicFeatureStatus(
    mainViewModel: MainActivityViewModel,
    onFeatureInstalled: () -> Unit
) {
    if (mainViewModel.loading.value) {
        DownloadingFeatureComposable()
        Log.d("SPLIT_TAG", "CheckDynamicFeatureStatus: show progress")
    }
    LaunchedEffect(
        key1 = mainViewModel.featureInstalled.value,
        key2 = mainViewModel.failureOccurred.value,
        key3 = mainViewModel.loading.value
    ) {
        if (mainViewModel.failureOccurred.value) {
            Log.d("SPLIT_TAG", "CheckDynamicFeatureStatus: failure")
            Toast.makeText(this@CheckDynamicFeatureStatus, "Failure occurred", Toast.LENGTH_SHORT).show()
        }
        if (mainViewModel.featureInstalled.value) {
            Log.d("SPLIT_TAG", "CheckDynamicFeatureStatus: installed")
            onFeatureInstalled()
        }
    }
}