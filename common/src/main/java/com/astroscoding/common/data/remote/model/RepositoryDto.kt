package com.astroscoding.common.data.remote.model

import com.astroscoding.common.data.mappers.DataMapper
import com.astroscoding.common.domain.model.Repo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepositoryDto(
    val allow_forking: Boolean?=null,
    val archive_url: String?=null,
    val archived: Boolean?=null,
    val assignees_url: String?=null,
    val blobs_url: String?=null,
    val branches_url: String?=null,
    val clone_url: String?=null,
    val collaborators_url: String?=null,
    val comments_url: String?=null,
    val commits_url: String?=null,
    val compare_url: String?=null,
    val contents_url: String?=null,
    val contributors_url: String?=null,
    val created_at: String?=null,
    val default_branch: String?=null,
    val deployments_url: String?=null,
    val description: String?=null,
    val disabled: Boolean?=null,
    val downloads_url: String?=null,
    val events_url: String?=null,
    val fork: Boolean?=null,
    val forks: Int?=null,
    val forks_count: Int?=null,
    val forks_url: String?=null,
    val full_name: String?=null,
    val git_commits_url: String?=null,
    val git_refs_url: String?=null,
    val git_tags_url: String?=null,
    val git_url: String?=null,
    val has_downloads: Boolean?=null,
    val has_issues: Boolean?=null,
    val has_pages: Boolean?=null,
    val has_projects: Boolean?=null,
    val has_wiki: Boolean?=null,
    val homepage: String?=null,
    val hooks_url: String?=null,
    val html_url: String?=null,
    val id: Long?=null,
    val is_template: Boolean?=null,
    val issue_comment_url: String?=null,
    val issue_events_url: String?=null,
    val issues_url: String?=null,
    val keys_url: String?=null,
    val labels_url: String?=null,
    val language: String?=null,
    val languages_url: String?=null,
    val license: License?=null,
    val merges_url: String?=null,
    val milestones_url: String?=null,
    val mirror_url: Any?=null,
    val name: String?=null,
    val node_id: String?=null,
    val notifications_url: String?=null,
    val open_issues: Int?=null,
    val open_issues_count: Int?=null,
    val owner: Owner?=null,
    val `private`: Boolean?=null,
    val pulls_url: String?=null,
    val pushed_at: String?=null,
    val releases_url: String?=null,
    val score: Double?=null,
    val size: Int?=null,
    val ssh_url: String?=null,
    val stargazers_count: Int?=null,
    val stargazers_url: String?=null,
    val statuses_url: String?=null,
    val subscribers_url: String?=null,
    val subscription_url: String?=null,
    val svn_url: String?=null,
    val tags_url: String?=null,
    val teams_url: String?=null,
    val topics: List<String>?=null,
    val trees_url: String?=null,
    val updated_at: String?=null,
    val url: String?=null,
    val visibility: String?=null,
    val watchers: Int?=null,
    val watchers_count: Int?=null,
    val web_commit_signoff_required: Boolean?=null
) : DataMapper<RepositoryDto, Repo> {

    override fun mapTo() = Repo(
        id = id?:0,
        name = name?:"",
        owner = com.astroscoding.common.domain.model.Owner(owner?.id ?:0, owner?.login?:"", owner?.avatar_url?:"", owner?.html_url?:""),
        description = description?:"",
        language = language?:"",
        starsCount = stargazers_count?:0,
        issuesCount = open_issues_count?:0,
        forksCount = forks_count?:0,
        licenseName = license?.name?:"",
        topics = topics?: listOf(""),
        htmlUrl = this.html_url ?:"",
        url = url ?: ""
    )
}