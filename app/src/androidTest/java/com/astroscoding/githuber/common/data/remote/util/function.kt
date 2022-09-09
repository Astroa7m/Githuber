package com.astroscoding.githuber.common.data.remote.util

import com.astroscoding.githuber.common.data.local.model.RepositoryEntity
import com.astroscoding.githuber.common.domain.model.Owner
import com.astroscoding.githuber.common.domain.model.Repo
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

object TestFunctions {
    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> getObjectFromJson(jsonString: String): T? {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter<T>()
        return adapter.fromJson(jsonString)
    }

    fun generateReposEntity(
        count: Int = 1,
        starsCounts: List<Int> = listOf(0, 0, 0),
        issuesCounts: List<Int> = listOf(0,0,0),
        forksCounts: List<Int> = listOf(0,0,0)
    ): List<RepositoryEntity> {
        val repoList = mutableListOf<RepositoryEntity>()
        repeat(count) { index ->
            val repo = generateRepoEntity(
                index.toLong(),
                starsCounts[index],
                issuesCounts[index],
                forksCounts[index]
            )

            repoList.add(repo)
        }
        return repoList
    }

    fun generateRepoEntity(
        id: Long,
        starsCount: Int = 0,
        issuesCount: Int = 0,
        forksCount: Int = 0
    ) = RepositoryEntity(
        id,
        "test",
        "test",
        "test",
        "test",
        "test",
        starsCount,
        issuesCount,
        forksCount,
        "test",
        "test"
    )

    fun generateReposDomain(count: Int = 1): List<Repo> {
        val repoList = mutableListOf<Repo>()
        repeat(count) {
            val repo = generateRepoDomain(it.toLong())
            repoList.add(repo)
        }
        return repoList
    }

    fun generateRepoDomain(id: Long) = Repo(
        id,
        "test",
        Owner(0, "test", "test"),
        "test",
        "test",
        0,
        0,
        0,
        "test",
        listOf("test")
    )

}