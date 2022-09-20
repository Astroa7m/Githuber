package com.astroscoding.common.util

import okio.buffer
import okio.source

object JsonReader {
    fun getJson(path: String): String {
        javaClass.classLoader?.getResourceAsStream(path)?.let {
            val source = it.source().buffer()
            return source.readString(Charsets.UTF_8)
        } ?: throw NullPointerException("File is null")
    }
}