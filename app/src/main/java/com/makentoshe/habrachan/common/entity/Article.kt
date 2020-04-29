package com.makentoshe.habrachan.common.entity

import android.content.res.Resources
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.model.article.html.*

@Entity
data class Article(
    /**
     * Special mark field. If set upped - this article was in last search response.
     * DO NOT USE THIS VALUE DIRECTLY.
     */
    var searchIndex: Int? = null,

    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @Embedded(prefix = "author_")
    @SerializedName("author")
    val author: User,
    @SerializedName("comments_count")
    val commentsCount: Int,
    @SerializedName("comments_new")
    val commentsNew: Int,
    @SerializedName("editor_version")
    val editorVersion: Int? = null,
    @SerializedName("favorites_count")
    val favoritesCount: Int,
    @SerializedName("flows")
    val flows: List<Flow> = listOf(),
    @SerializedName("full_url")
    val fullUrl: String,
    @SerializedName("has_polls")
    val hasPolls: Boolean,
    @SerializedName("hubs")
    val hubs: List<Hub> = listOf(),
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
    @Embedded(prefix = "metadata_")
    @SerializedName("metadata")
    val metadata: Metadata? = null,
    @SerializedName("path")
    val path: String,
    @SerializedName("post_type")
    val postType: Int,
    @SerializedName("post_type_str")
    val postTypeStr: String,
    @SerializedName("preview_html")
    val previewHtml: String,
    @SerializedName("text_html")
    val textHtml: String? = null,
    @SerializedName("reading_count")
    val readingCount: Int,
    @SerializedName("score")
    val score: Int,
    @SerializedName("source_author")
    val sourceAuthor: String? = null,
    @SerializedName("source_link")
    val sourceLink: String? = null,
    @SerializedName("tags_string")
    val tagsString: String,
    @SerializedName("text_cut")
    val textCut: String? = null,
    @SerializedName("time_interesting")
    val timeInteresting: String? = null,
    @SerializedName("time_published")
    val timePublished: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("vote")
    val vote: Double? = null,
    @SerializedName("votes_count")
    val votesCount: Int,
    @SerializedName("is_can_comment")
    val isCanComment: Boolean? = null
//    @SerializedName("voting")
//    val voting: Voting? = null
) {

    fun buildHtml(resources: Resources): String {
        val builder = HtmlBuilder(this)
        builder.addAddon(DisplayScriptAddon(resources))
        builder.addAddon(StyleAddon(resources))
        builder.addAddon(SpoilerAddon())
        builder.addAddon(ImageAddon())
        return builder.build()
    }

    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(json: String): Article {
            return Gson().fromJson(json, Article::class.java)
        }
    }

}