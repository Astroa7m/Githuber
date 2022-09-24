package com.astroscoding.common.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repo(
    val id: Long,
    val name: String,
    val owner: Owner,
    val description: String,
    val language: String,
    val starsCount: Int,
    val issuesCount: Int,
    val forksCount: Int,
    val licenseName: String,
    val topics: List<String>,
    val htmlUrl: String,
    val url: String
) : Parcelable
