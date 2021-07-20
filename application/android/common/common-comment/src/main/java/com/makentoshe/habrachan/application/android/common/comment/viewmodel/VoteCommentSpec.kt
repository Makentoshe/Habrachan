package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.CommentVote


/** Spec for voting comment */
data class VoteCommentSpec(val commentId: CommentId, val commentVote: CommentVote)