package com.makentoshe.habrachan.network

data class GetContentResponse(val request: GetContentRequest, val bytes: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GetContentResponse

        if (request != other.request) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = request.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}