package com.makentoshe.habrachan.entity.natives

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.PostedComment

data class PostedComment(
    @SerializedName("id")
    override val commentId: Int, // 21912544
    @SerializedName("message")
    override val message: String, // Ну Гугол тут, как всегда, отличился. Что в плей маркете с ними проблемы возникают, что здесь.
    @SerializedName("time_published")
    override val rawTimePublished: String, // 2020-08-01T11:14:14+03:00
    @SerializedName("on_moderated")
    override val onModerated: Boolean, // false
) : PostedComment