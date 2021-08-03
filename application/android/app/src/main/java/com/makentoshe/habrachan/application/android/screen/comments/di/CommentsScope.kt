package com.makentoshe.habrachan.application.android.screen.comments.di

interface CommentsScope

data class ArticleCommentsScope2(val articleId: Int): CommentsScope

data class DiscussionCommentsScope2(val commentId: Int): CommentsScope
