package com.makentoshe.habrachan.application.common.arena.user.get

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.*
import com.makentoshe.habrachan.entity.user.component.UserAvatar
import com.makentoshe.habrachan.entity.user.component.UserFullName
import com.makentoshe.habrachan.entity.user.component.UserLogin
import com.makentoshe.habrachan.entity.user.get.component.TimeLastActivity
import com.makentoshe.habrachan.entity.user.get.component.TimeRegistered
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonPrimitive

data class UserFromArena(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val UserFromArena.login by requireReadonlyProperty(propertyMapper("alias") { jsonElement ->
    UserLogin(jsonElement.jsonPrimitive.content)
})

val UserFromArena.avatar by optionReadonlyProperty(propertyMapper("avatarUrl") { jsonElement ->
    return@propertyMapper if (jsonElement is JsonNull) null else UserAvatar(jsonElement.jsonPrimitive.content)
})

val UserFromArena.fullname by requireReadonlyProperty(propertyMapper("fullname") { jsonElement ->
    UserFullName(jsonElement.jsonPrimitive.content)
})

val UserFromArena.speciality by requireReadonlyProperty(stringPropertyMapper("speciality"))

val UserFromArena.registered by requireReadonlyProperty(propertyMapper("registerDateTime") { jsonElement ->
    TimeRegistered(jsonElement.jsonPrimitive.content)
})

val UserFromArena.lastActivity by requireReadonlyProperty(propertyMapper("lastActivityDateTime") { jsonElement ->
    TimeLastActivity(jsonElement.jsonPrimitive.content)
})

val UserFromArena.rating by requireReadonlyProperty(intPropertyMapper("rating"))

val UserFromArena.isReadonly by requireReadonlyProperty(booleanPropertyMapper("isReadonly"))

val UserFromArena.canBeInvited by requireReadonlyProperty(booleanPropertyMapper("canBeInvited"))

val UserFromArena.postCount by requireReadonlyProperty(intPropertyMapper("counterStats", "postCount"))

val UserFromArena.favoriteCount by requireReadonlyProperty(intPropertyMapper("counterStats", "favoriteCount"))

val UserFromArena.commentCount by requireReadonlyProperty(intPropertyMapper("counterStats", "commentCount"))

val UserFromArena.followCount by requireReadonlyProperty(intPropertyMapper("followStats", "followCount"))

val UserFromArena.followersCount by requireReadonlyProperty(intPropertyMapper("followStats", "followersCount"))

val UserFromArena.scoreCount by requireReadonlyProperty(intPropertyMapper("scoreStats", "score"))

val UserFromArena.votesCount by requireReadonlyProperty(intPropertyMapper("scoreStats", "votesCount"))

//val birthday: Any, // null
//val gender: String, // 0
//val location: Any, // null
//val ratingPos: Any, // null
//val relatedData: Any, // null
//val workplace: List<Any>
