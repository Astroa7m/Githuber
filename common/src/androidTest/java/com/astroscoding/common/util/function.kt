package com.astroscoding.common.util

import com.astroscoding.common.data.local.model.RepositoryEntity
import com.astroscoding.common.domain.model.Owner
import com.astroscoding.common.domain.model.Repo

object TestFunctions {
    fun generateReposEntity(
        count: Int = 1,
        starsCounts: List<Int> = IntArray(count){0}.toList(),
        issuesCounts: List<Int> = IntArray(count){0}.toList(),
        forksCounts: List<Int> = IntArray(count){0}.toList()
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
        "test",
        issuesCount = issuesCount,
        forksCount = forksCount,
        starsCount = starsCount,
        htmlUrl = "test",
        licenseName = "test",
        topics = "test"
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
        Owner(0, "test", "test", "test"),
        "test",
        "test",
        0,
        0,
        0,
        "test",
        listOf("test"),
        ""
    )

}