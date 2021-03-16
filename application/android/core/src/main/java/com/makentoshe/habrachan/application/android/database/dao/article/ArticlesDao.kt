package com.makentoshe.habrachan.application.android.database.dao.article

import com.makentoshe.habrachan.application.android.database.record.article.ArticleRecord

interface ArticlesDao<T: ArticleRecord> {

    val title: String

    fun getAll(): List<T>

    fun getById(id: Int): T?

    fun getTimePublishedDescSorted(offset: Int, count: Int): List<T>

    fun insert(employee: T)

    fun delete(employee: T)

    fun clear()
}
