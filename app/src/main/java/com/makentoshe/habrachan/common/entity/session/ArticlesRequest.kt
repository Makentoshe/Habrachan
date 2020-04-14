package com.makentoshe.habrachan.common.entity.session

import android.content.Context
import androidx.room.Entity
import com.makentoshe.habrachan.R

@Entity
data class ArticlesRequest(val request: String) {

    init {
        if (!types.contains(request)) throw IllegalArgumentException(request)
    }

    fun toString(context: Context): String = when(request){
        INTERESTING -> context.getString(R.string.articles_type_interesting)
        ALL -> context.getString(R.string.articles_type_all)
        else -> throw IllegalStateException()
    }

    companion object {
        private const val INTERESTING = "posts/interesting"
        private const val ALL = "posts/all"
        private val types = setOf(INTERESTING, ALL)

        fun interesting() = ArticlesRequest(INTERESTING)

        fun all() = ArticlesRequest(ALL)
    }
}