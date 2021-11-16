package com.makentoshe.habrachan.entity.mobile.me

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.intPropertyMapper
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import kotlinx.serialization.json.JsonElement

data class ScoreStats(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val ScoreStats.score by requireReadonlyProperty(intPropertyMapper("score"))

val ScoreStats.votesCount by requireReadonlyProperty(intPropertyMapper("votesCount"))