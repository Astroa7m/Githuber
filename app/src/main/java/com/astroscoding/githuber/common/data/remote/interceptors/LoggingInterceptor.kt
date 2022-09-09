package com.astroscoding.githuber.common.data.remote.interceptors

import android.util.Log
import com.astroscoding.githuber.common.util.Constants
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class LoggingInterceptor @Inject constructor(): HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Log.d(Constants.LOGGING_TAG, "log: $message")
    }
}