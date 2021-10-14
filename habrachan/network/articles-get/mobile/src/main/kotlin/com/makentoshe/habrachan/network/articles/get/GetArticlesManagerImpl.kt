package com.makentoshe.habrachan.network.articles.get

import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApi
import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.articles
import com.makentoshe.habrachan.api.mobile.articles.build
import com.makentoshe.habrachan.api.mobile.articles.filter
import io.ktor.client.*

class GetArticlesManagerImpl(client: HttpClient) : GetArticlesManager(client) {

    override fun url(request: GetArticlesRequest): HabrArticlesFilterApi {
        return MobileHabrApi.articles().filter(*request.filters).build(request.parameters)
    }

}