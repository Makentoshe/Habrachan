package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import com.makentoshe.habrachan.entity.CommentVote


/** Spec for voting comment */
data class VoteCommentSpec(val commentId: Int, val commentVote: CommentVote)