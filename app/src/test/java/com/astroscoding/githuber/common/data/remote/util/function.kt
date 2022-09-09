package com.astroscoding.githuber.common.data.remote.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

object TestFunctions {
    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> getObjectFromJson(jsonString: String): T? {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter<T>()
        return adapter.fromJson(jsonString)
    }

}