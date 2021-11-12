package com.makentoshe.habrachan.application.android.screen.articles.model

import com.makentoshe.habrachan.application.android.database.user.record.ArticlesUserSearchRecord

data class ArticlesUserSearch(val title: String, val isDefault: Boolean)

fun ArticlesUserSearchRecord.toArticlesUserSearch(): ArticlesUserSearch {
    return ArticlesUserSearch(title, isDefault)
}
