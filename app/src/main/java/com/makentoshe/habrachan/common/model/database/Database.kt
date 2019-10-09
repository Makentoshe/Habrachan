package com.makentoshe.habrachan.common.model.database

interface Database {

    fun clear()

    class Builder {
        fun posts(title: String): DatabasePosts.Builder {
            return DatabasePosts.Builder(title)
        }
    }
}

