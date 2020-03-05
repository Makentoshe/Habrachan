package com.makentoshe.habrachan.model.article

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.repository.Repository
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class GetArticle(
    private val disposables: CompositeDisposable,
    private val articleRepository: Repository<Int, Single<Article>>,
    private val articleId: Int
) {
}