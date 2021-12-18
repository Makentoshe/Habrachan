package com.makentoshe.habrachan.application.common.arena.user.get

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.*
import com.makentoshe.habrachan.entity.user.component.UserAvatar
import com.makentoshe.habrachan.entity.user.component.UserFullName
import com.makentoshe.habrachan.entity.user.component.UserGender
import com.makentoshe.habrachan.entity.user.component.UserLogin
import com.makentoshe.habrachan.entity.user.get.component.TimeLastActivity
import com.makentoshe.habrachan.entity.user.get.component.TimeRegistered
import kotlinx.serialization.json.*

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

val UserFromArena.ratingPosition by optionReadonlyProperty(intPropertyMapper("ratingPos"))

//val UserFromArena.isReadonly by requireReadonlyProperty(booleanPropertyMapper("isReadonly"))
//
//val UserFromArena.canBeInvited by requireReadonlyProperty(booleanPropertyMapper("canBeInvited"))
//
//val UserFromArena.postCount by requireReadonlyProperty(intPropertyMapper("counterStats", "postCount"))
//
//val UserFromArena.favoriteCount by requireReadonlyProperty(intPropertyMapper("counterStats", "favoriteCount"))
//
//val UserFromArena.commentCount by requireReadonlyProperty(intPropertyMapper("counterStats", "commentCount"))
//
//val UserFromArena.followCount by requireReadonlyProperty(intPropertyMapper("followStats", "followCount"))
//
//val UserFromArena.followersCount by requireReadonlyProperty(intPropertyMapper("followStats", "followersCount"))

val UserFromArena.scoreCount by requireReadonlyProperty(intPropertyMapper("scoreStats", "score"))

val UserFromArena.votesCount by requireReadonlyProperty(intPropertyMapper("scoreStats", "votesCount"))

val UserFromArena.gender by requireReadonlyProperty(propertyMapper("gender") { jsonElement ->
    return@propertyMapper UserGender.valueOf(jsonElement.jsonPrimitive.content.toInt())
})

val UserFromArena.birthday by optionReadonlyProperty(stringPropertyMapper("birthday"))

fun userFromArena(
    login: String,
    fullname: String,
    avatarUrl: String?,
    speciality: String,
    gender: Int,
    rating: Int,
    ratingPosition: Int?,
    scoresCount: Int,
    votesCount: Int,
    lastActivityDateTimeRaw: String,
    registerDateTimeRaw: String,
    birthdayRaw: String?,
) = mapOf(
    "alias" to JsonPrimitive(login),
    "fullname" to JsonPrimitive(fullname),
    "avatarUrl" to JsonPrimitive(avatarUrl),
    "speciality" to JsonPrimitive(speciality),
    "gender" to JsonPrimitive(gender.toString()),
    "rating" to JsonPrimitive(rating),
    "ratingPos" to JsonPrimitive(ratingPosition),
    "scoreStats" to JsonObject(mapOf("score" to JsonPrimitive(scoresCount), "votesCount" to JsonPrimitive(votesCount))),
    "scoresCount" to JsonPrimitive(scoresCount),
    "lastActivityDateTime" to JsonPrimitive(lastActivityDateTimeRaw),
    "registerDateTime" to JsonPrimitive(registerDateTimeRaw),
    "birthday" to JsonPrimitive(birthdayRaw)
).let(::UserFromArena)

//val location: Any, // null
//val relatedData: Any, // null
//val workplace: List<Any>
