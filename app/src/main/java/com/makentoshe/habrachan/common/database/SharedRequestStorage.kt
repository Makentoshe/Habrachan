package com.makentoshe.habrachan.common.database

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.makentoshe.habrachan.common.network.request.GetPostsRequest

class SharedRequestStorage private constructor(private val sharedPreferences: SharedPreferences) : RequestStorage {

    override fun getOrDefault(page: Int, default: GetPostsRequest): GetPostsRequest {
        return get(page) ?: default
    }

    override fun get(page: Int): GetPostsRequest? {
        val api = sharedPreferences.getString(API, null)
        val token = sharedPreferences.getString(TOKEN, null)
        if (token == null && api == null) return null
        val path1 = sharedPreferences.getString(PATH1, null) ?: return null
        val path2 = sharedPreferences.getString(PATH2, null) ?: return null
        val client = sharedPreferences.getString(CLIENT, null) ?: return null
        val sort = sharedPreferences.getString(SORT, null)
        val query = sharedPreferences.getString(QUERY, null)
        val article = sharedPreferences.getBoolean(ARTICLE, false)
        return GetPostsRequest(path1, path2, client, token, api, page, query, sort, article)
    }

    @SuppressLint("ApplySharedPref")
    override fun set(request: GetPostsRequest) {
        val edit = sharedPreferences.edit()
        edit.putString(CLIENT, request.client)
        edit.putString(API, request.api)
        edit.putString(TOKEN, request.token)
        edit.putString(PATH1, request.path1)
        edit.putString(PATH2, request.path2)
        edit.putString(SORT, request.sort)
        edit.putString(QUERY, request.query)
        if (request.getArticle != null) {
            edit.putBoolean(ARTICLE, request.getArticle)
        }
        edit.commit()
    }

    companion object {
        private const val SORT = "Sort"
        private const val ARTICLE = "Article"
        private const val QUERY = "Query"
        private const val PATH1 = "Path1"
        private const val PATH2 = "Path2"
        private const val CLIENT = "Client"
        private const val API = "Api"
        private const val TOKEN = "Token"
    }

    class Builder {

        fun build(context: Context): RequestStorage {
            val title = SharedRequestStorage::class.java.simpleName
            val sharedPreferences = context.getSharedPreferences(title, Context.MODE_PRIVATE)
            return SharedRequestStorage(sharedPreferences)
        }
    }
}