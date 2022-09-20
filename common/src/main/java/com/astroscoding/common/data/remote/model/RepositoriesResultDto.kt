package com.astroscoding.common.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepositoriesResultDto(
    @field:Json(name = "incomplete_results")val incomplete_results: Boolean,
    @field:Json(name = "items") val repositories: List<RepositoryDto>,
    @field:Json(name = "total_count")val total_count: Int
)

