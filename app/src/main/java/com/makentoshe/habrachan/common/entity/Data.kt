package com.makentoshe.habrachan.common.entity


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("author")
    val author: User,
    @SerializedName("comments_count")
    val commentsCount: Int,
    @SerializedName("comments_new")
    val commentsNew: Int,
    @SerializedName("favorites_count")
    val favoritesCount: Int,
    @SerializedName("flows")
    val flows: List<Flow>,
    @SerializedName("full_url")
    val fullUrl: String,
    @SerializedName("has_polls")
    val hasPolls: Boolean,
    @SerializedName("hubs")
    val hubs: List<Hub>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_can_vote")
    val isCanVote: Boolean,
    @SerializedName("is_comments_hide")
    val isCommentsHide: Int,
    @SerializedName("is_corporative")
    val isCorporative: Int,
    @SerializedName("is_favorite")
    val isFavorite: Boolean,
    @SerializedName("is_habred")
    val isHabred: Boolean,
    @SerializedName("is_interesting")
    val isInteresting: Boolean,
    @SerializedName("is_recovery_mode")
    val isRecoveryMode: Boolean,
    @SerializedName("is_tutorial")
    val isTutorial: Boolean,
    @SerializedName("lang")
    val lang: String,
    @SerializedName("metadata")
    val metadata: Metadata?,
    @SerializedName("path")
    val path: String,
    @SerializedName("post_type")
    val postType: Int,
    @SerializedName("post_type_str")
    val postTypeStr: String,
    @SerializedName("preview_html")
    val previewHtml: String,
    @SerializedName("text_html")
    val textHtml: String?,
    @SerializedName("reading_count")
    val readingCount: Int,
    @SerializedName("score")
    val score: Int,
    @SerializedName("source_author")
    val sourceAuthor: String,
    @SerializedName("source_link")
    val sourceLink: String,
    @SerializedName("tags_string")
    val tagsString: String,
    @SerializedName("text_cut")
    val textCut: String,
    @SerializedName("time_interesting")
    val timeInteresting: String,
    @SerializedName("time_published")
    val timePublished: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("vote")
    val vote: Any,
    @SerializedName("votes_count")
    val votesCount: Int,
    @SerializedName("is_can_comment")
    val isCanComment: Boolean?
)