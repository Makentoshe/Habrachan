package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.entity.*

@Entity
data class ArticleAuthorRecord(
    @PrimaryKey
    val userId: Int, val login: String, val fullname: String?, val avatar: String
)  {

    constructor(articleAuthor: ArticleAuthor) : this(
        articleAuthor.userId, articleAuthor.login, articleAuthor.fullname, articleAuthor.avatar
    )

    fun toArticleAuthor() = articleAuthor(userId, avatar, login, fullname)
}
