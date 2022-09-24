package com.astroscoding.common.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Owner(
    val id: Long,
    val username: String,
    val avatarUrl: String,
    val htmlUrl: String
): Parcelable
