package com.makentoshe.habrachan.application.common.arena.content

import com.makentoshe.habrachan.entity.content.get.NetworkContent

class ContentFromArena(val bytes: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NetworkContent

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}