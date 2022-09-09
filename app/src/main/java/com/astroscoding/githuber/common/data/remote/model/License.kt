package com.astroscoding.githuber.common.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class License(
    val key: String?,
    val name: String?,
    val node_id: String?,
    val spdx_id: String?,
    val url: String?
)