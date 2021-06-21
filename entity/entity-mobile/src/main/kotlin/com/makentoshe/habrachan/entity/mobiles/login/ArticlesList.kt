package com.makentoshe.habrachan.entity.mobiles.login

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.mobiles.Article

data class ArticlesList(
    @SerializedName("articlesIds")
    val articlesIds: ArticlesIds,
    @SerializedName("articlesList")
    val articleRefs: HashMap<Int, Article>,
    @SerializedName("isLoading")
    val isLoading: Boolean, // false
    @SerializedName("karma")
    val karma: Any,
    @SerializedName("lastVisitedRoute")
    val lastVisitedRoute: Any,
    @SerializedName("pagesCount")
    val pagesCount: PagesCount,
    @SerializedName("reasonsList")
    val reasonsList: Any?, // null
    @SerializedName("route")
    val route: Route,
    @SerializedName("view")
    val view: String // cards
)