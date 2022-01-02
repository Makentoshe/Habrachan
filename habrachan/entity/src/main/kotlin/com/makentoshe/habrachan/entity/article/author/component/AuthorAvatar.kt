package com.makentoshe.habrachan.entity.article.author.component

import java.net.MalformedURLException
import java.net.URL

@JvmInline
value class AuthorAvatar(val avatarUrl: String)

// Tries to create an instance of URL class and fix "no protocol" issue if it occurs
val AuthorAvatar.url
    get() = try {
        URL(avatarUrl)
    } catch (exception: MalformedURLException) {
        URL("https:$avatarUrl")
    }
