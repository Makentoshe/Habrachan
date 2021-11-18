package com.makentoshe.habrachan.entity.me.mobile

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.intPropertyMapper
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import kotlinx.serialization.json.JsonElement

data class ChargeSettings(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val ChargeSettings.postVoteCount by requireReadonlyProperty(intPropertyMapper("postVoteCount"))

val ChargeSettings.commentVoteCount by requireReadonlyProperty(intPropertyMapper("commentVoteCount"))