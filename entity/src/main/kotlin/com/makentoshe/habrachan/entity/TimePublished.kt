package com.makentoshe.habrachan.entity

interface TimePublished {
    val rawTimePublished: String
}

fun TimePublished.asString(): String {
    return "TimePublished(rawTimePublished=$rawTimePublished)"
}

fun timePublished(raw: String) = object : TimePublished {
    override val rawTimePublished: String = raw
}