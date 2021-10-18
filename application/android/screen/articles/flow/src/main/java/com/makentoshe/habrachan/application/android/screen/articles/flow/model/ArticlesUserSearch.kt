package com.makentoshe.habrachan.application.android.screen.articles.flow.model

import com.makentoshe.habrachan.application.android.database.record.userdatabase.ArticlesUserSearchRecord

data class ArticlesUserSearch(val title: String, val isDefault: Boolean)

fun ArticlesUserSearchRecord.toArticlesUserSearch(): ArticlesUserSearch {
    return ArticlesUserSearch(title, isDefault)
}
