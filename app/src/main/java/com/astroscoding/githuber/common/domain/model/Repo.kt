package com.astroscoding.githuber.common.domain.model


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
    val topics: List<String>
)
