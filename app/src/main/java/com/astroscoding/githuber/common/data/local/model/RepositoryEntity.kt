package com.astroscoding.githuber.common.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.astroscoding.githuber.common.data.mappers.DataMapper
import com.astroscoding.githuber.common.domain.model.Owner
import com.astroscoding.githuber.common.domain.model.Repo

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val owner: String,
    val ownerPhotoUrl: String,
    val description: String,
    val languages: String,
    val starsCount: Int,
    val issuesCount: Int,
    val forksCount: Int,
    val licenseName: String,
    val topics: String
) : DataMapper<RepositoryEntity, Repo>{

    override fun mapTo() = Repo(
        id,
        name,
        Owner(0, owner, ownerPhotoUrl),
        description,
        languages,
        starsCount,
        issuesCount,
        forksCount,
        licenseName,
        topics.split(", ")
    )

    companion object{
        fun mapFrom(anotherEntity: Repo) = RepositoryEntity(
            anotherEntity.id,
            anotherEntity.name,
            anotherEntity.owner.username,
            anotherEntity.owner.avatarUrl,
            anotherEntity.description,
            anotherEntity.language,
            anotherEntity.starsCount,
            anotherEntity.issuesCount,
            anotherEntity.forksCount,
            anotherEntity.licenseName,
            anotherEntity.topics.joinToString()
        )
    }
}
