package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest2
import com.makentoshe.habrachan.network.response.GetCommentsResponse

interface GetCommentsManager<Request: GetCommentsRequest2> {

    fun request(userSession: UserSession, articleId: Int): Request

    fun comments(request: Request): Result<GetCommentsResponse>
}
