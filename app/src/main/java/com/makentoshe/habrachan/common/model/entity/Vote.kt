package com.makentoshe.habrachan.common.model.entity

sealed class Vote {
    abstract fun toScore(): Int

    object VoteUp: Vote() {
        override fun toScore() = 1
    }

    object VoteDown: Vote() {
        override fun toScore() = -1
    }
}
