package com.astroscoding.sharing.model

import com.astroscoding.common.domain.model.Repo

data class RepoToShare(
    val name: String,
    val description: String,
    val ownerName: String,
    val ownerAvatarUrl: String
){
    companion object{
        fun mapFromDomain(domainRepo: Repo): RepoToShare{
            return RepoToShare(
                name = domainRepo.name,
                description = "${domainRepo.name} ◉ ${domainRepo.description}",
                ownerName = domainRepo.owner.username,
                ownerAvatarUrl = domainRepo.owner.avatarUrl
            )
        }
    }
}
