package com.astroscoding.common.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.astroscoding.common.data.mappers.DataMapper
import com.astroscoding.common.domain.model.Owner
import com.astroscoding.common.domain.model.Repo

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val owner: String,
    val ownerPhotoUrl: String,
    val ownerHtmlUrl: String,
    val description: String,
    val languages: String,
    val starsCount: Int,
    val issuesCount: Int,
    val forksCount: Int,
    val licenseName: String,
    val topics: String,
    val htmlUrl: String
) : DataMapper<RepositoryEntity, Repo> {

    override fun mapTo() = Repo(
        id,
        name,
        Owner(0, owner, ownerPhotoUrl, ownerHtmlUrl),
        description,
        languages,
        starsCount,
        issuesCount,
        forksCount,
        licenseName,
        topics.split(", "),
        htmlUrl
    )

    companion object{
        fun mapFrom(anotherEntity: Repo) = RepositoryEntity(
            anotherEntity.id,
            anotherEntity.name,
            anotherEntity.owner.username,
            anotherEntity.owner.avatarUrl,
            anotherEntity.owner.htmlUrl,
            anotherEntity.description,
            anotherEntity.language,
            anotherEntity.starsCount,
            anotherEntity.issuesCount,
            anotherEntity.forksCount,
            anotherEntity.licenseName,
            anotherEntity.topics.joinToString(),
            anotherEntity.htmlUrl
        )
    }
}
