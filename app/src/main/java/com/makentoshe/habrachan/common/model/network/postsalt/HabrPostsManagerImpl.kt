package com.makentoshe.habrachan.common.model.network.postsalt

import com.makentoshe.habrachan.common.entity.post.PostResponse
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import io.reactivex.Single

class HabrPostsManagerImpl (
    private val api: HabrPostsApi
) : HabrPostsManager {

    override fun getPosts(request: GetPostsRequest): Single<PostsResponse> {
        return api.getPosts(
            clientKey = request.client,
            token = request.token,
            apiKey = request.api,
            type1 = request.path1,
            type2 = request.path2,
            page = request.page,
            include = null,
            getArticle = null,
            exclude = null
        )
    }

    override fun getPostsWithBody(request: GetPostsRequest): Single<PostsResponse> {
        return api.getPosts(
            clientKey = request.client,
            token = request.token,
            apiKey = request.api,
            type1 = request.path1,
            type2 = request.path2,
            page = request.page,
            include = "text_html",
            getArticle = null,
            exclude = null
        )
    }

    override fun getPost(request: GetPostRequest): Single<PostResponse> {
        return api.getPost(
            clientKey = request.client,
            token = request.token,
            apiKey = request.api,
            id = request.id,
            getArticle = null,
            include = null,
            exclude = null
        )
    }

}