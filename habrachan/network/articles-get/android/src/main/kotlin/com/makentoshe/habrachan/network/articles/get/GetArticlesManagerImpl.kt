package com.makentoshe.habrachan.network.articles.get

import com.makentoshe.habrachan.api.android.AndroidHabrApi
import com.makentoshe.habrachan.api.android.articles
import com.makentoshe.habrachan.api.android.articles.build
import com.makentoshe.habrachan.api.android.articles.filter
import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApi
import io.ktor.client.*

class GetArticlesManagerImpl(client: HttpClient) : GetArticlesManager(client) {

    override fun url(request: GetArticlesRequest): HabrArticlesFilterApi {
        return AndroidHabrApi.articles().filter(*request.filters).build(request.parameters)
    }

}