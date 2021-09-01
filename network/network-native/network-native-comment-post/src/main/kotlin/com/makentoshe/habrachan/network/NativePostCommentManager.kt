package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.manager.PostCommentManager
import com.makentoshe.habrachan.network.natives.api.NativeCommentsApi
import com.makentoshe.habrachan.network.response.PostCommentResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class NativePostCommentManager internal constructor(
    private val api: NativeCommentsApi,
    private val deserializer: NativePostCommentDeserializer,
) : PostCommentManager<NativePostCommentRequest> {

    override fun request(userSession: UserSession, articleId: ArticleId, text: String, parent: CommentId, ): NativePostCommentRequest {
        return NativePostCommentRequest(userSession, articleId, text, parent)
    }

    override fun post(request: NativePostCommentRequest): Result<PostCommentResponse> = try {
        postCommentApi(request).deserialize(request)
    } catch (exception: Exception) {
        Result.failure(NativePostCommentException(request, message = exception.localizedMessage, cause = exception))
    }

    private fun postCommentApi(request: NativePostCommentRequest) : Response<ResponseBody> {
        return api.postComment(
            clientKey = request.userSession.client,
            token = request.userSession.token,
            articleId = request.articleId.articleId,
            text = request.text,
            commentId = request.parentId.commentId
        ).execute()
    }

    private fun Response<ResponseBody>.deserialize(request: NativePostCommentRequest): Result<NativePostCommentResponse> {
        return fold({ successBody ->
            deserializer.body(request, successBody.string())
        }, { failureBody ->
            deserializer.error(request, failureBody.string())
        })
    }


    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"
        private val deserializer = NativePostCommentDeserializer()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativePostCommentManager(getRetrofit().create(NativeCommentsApi::class.java), deserializer)
    }
}