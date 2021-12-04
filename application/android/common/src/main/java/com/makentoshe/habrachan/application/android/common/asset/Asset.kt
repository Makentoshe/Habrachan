package com.makentoshe.habrachan.application.android.common.asset

data class Asset(val name: String, val bytes: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Asset

        if (name != other.name) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }

    fun change(action: (ByteArray) -> ByteArray) : Asset {
        return Asset(name, action(bytes))
    }
}
