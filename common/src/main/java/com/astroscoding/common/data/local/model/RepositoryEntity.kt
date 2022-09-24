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
    val htmlUrl: String,
    val url: String
) : DataMapper<RepositoryEntity, Repo> {

    override fun mapTo() = Repo(
        id = id,
        name = name,
        owner = Owner(0, owner, ownerPhotoUrl, ownerHtmlUrl),
        description = description,
        language = languages,
        starsCount = starsCount,
        issuesCount = issuesCount,
        forksCount = forksCount,
        licenseName = licenseName,
        topics = topics.split(", "),
        htmlUrl = htmlUrl,
        url = url
    )

    companion object{
        fun mapFrom(anotherEntity: Repo) = RepositoryEntity(
            id = anotherEntity.id,
            name = anotherEntity.name,
            owner = anotherEntity.owner.username,
            ownerPhotoUrl = anotherEntity.owner.avatarUrl,
            ownerHtmlUrl = anotherEntity.owner.htmlUrl,
            description = anotherEntity.description,
            languages = anotherEntity.language,
            starsCount = anotherEntity.starsCount,
            issuesCount = anotherEntity.issuesCount,
            forksCount = anotherEntity.forksCount,
            licenseName = anotherEntity.licenseName,
            topics = anotherEntity.topics.joinToString(),
            htmlUrl = anotherEntity.htmlUrl,
            url = anotherEntity.url
        )
    }
}
