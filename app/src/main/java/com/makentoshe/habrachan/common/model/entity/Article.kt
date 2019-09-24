package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("author")
    val author: Author,
    @SerializedName("comments_count")
    val commentsCount: String,
    @SerializedName("comments_new")
    val commentsNew: Any,
    @SerializedName("favorites_count")
    val favoritesCount: String,
    @SerializedName("flows")
    val flows: List<Flow>,
    @SerializedName("full_url")
    val fullUrl: String,
    @SerializedName("has_polls")
    val hasPolls: Boolean,
    @SerializedName("hubs")
    val hubs: List<Hub>,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: List<Any>,
    @SerializedName("is_can_vote")
    val isCanVote: Boolean,
    @SerializedName("is_comments_hide")
    val isCommentsHide: String,
    @SerializedName("is_corporative")
    val isCorporative: String,
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
    val metadata: Metadata,
    @SerializedName("path")
    val path: String,
    @SerializedName("post_type")
    val postType: String,
    @SerializedName("post_type_str")
    val postTypeStr: String,
    @SerializedName("preview_html")
    val previewHtml: String,
    @SerializedName("reading_count")
    val readingCount: Int,
    @SerializedName("score")
    val score: String,
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
    val votesCount: String
)