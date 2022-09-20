package com.astroscoding.common.util.util

import com.astroscoding.common.domain.model.Owner
import com.astroscoding.common.domain.model.Repo
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

object TestFunctions {
    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> getObjectFromJson(jsonString: String): T? {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter<T>()
        return adapter.fromJson(jsonString)
    }

    fun generateReposDomain(
        count: Int = 1,
        starsCounts: List<Int> = IntArray(count){0}.toList(),
        issuesCounts: List<Int> = IntArray(count){0}.toList(),
        forksCounts: List<Int> = IntArray(count){0}.toList()
    ): List<Repo> {
        val repoList = mutableListOf<Repo>()
        repeat(count) { index ->
            val repo = generateRepoDomain(
                index.toLong(),
                starsCounts[index],
                issuesCounts[index],
                forksCounts[index]
            )
            repoList.add(repo)
        }
        return repoList
    }

    private fun generateRepoDomain(
        id: Long,
        starsCount: Int = 0,
        issuesCount: Int = 0,
        forksCount: Int = 0
    ) = Repo(
        id,
        "test",
        Owner(0, "test", "test", "test"),
        "test",
        "test",
        starsCount,
        issuesCount,
        forksCount,
        "test",
        listOf("test"),
        "test"
    )

}