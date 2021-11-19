package com.makentoshe.habrachan.entity.user.get

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

data class NetworkUser(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val NetworkUser.login by requireReadonlyProperty(propertyMapper("alias") { jsonElement ->
    UserLogin(jsonElement.jsonPrimitive.content)
})

val NetworkUser.avatar by optionReadonlyProperty(propertyMapper("avatarUrl") { jsonElement ->
    return@propertyMapper if (jsonElement is JsonNull) null else UserAvatar(jsonElement.jsonPrimitive.content)
})

val NetworkUser.fullname by requireReadonlyProperty(propertyMapper("fullname") { jsonElement ->
    UserFullName(jsonElement.jsonPrimitive.content)
})

val NetworkUser.speciality by requireReadonlyProperty(stringPropertyMapper("speciality"))

val NetworkUser.registered by requireReadonlyProperty(propertyMapper("registerDateTime") { jsonElement ->
    TimeRegistered(jsonElement.jsonPrimitive.content)
})

val NetworkUser.lastActivity by requireReadonlyProperty(propertyMapper("lastActivityDateTime") { jsonElement ->
    TimeLastActivity(jsonElement.jsonPrimitive.content)
})

val NetworkUser.rating by requireReadonlyProperty(intPropertyMapper("rating"))

val NetworkUser.isReadonly by requireReadonlyProperty(booleanPropertyMapper("isReadonly"))

val NetworkUser.canBeInvited by requireReadonlyProperty(booleanPropertyMapper("canBeInvited"))

val NetworkUser.postCount by requireReadonlyProperty(intPropertyMapper("counterStats", "postCount"))

val NetworkUser.favoriteCount by requireReadonlyProperty(intPropertyMapper("counterStats", "favoriteCount"))

val NetworkUser.commentCount by requireReadonlyProperty(intPropertyMapper("counterStats", "commentCount"))

val NetworkUser.followCount by requireReadonlyProperty(intPropertyMapper("followStats", "followCount"))

val NetworkUser.followersCount by requireReadonlyProperty(intPropertyMapper("followStats", "followersCount"))

val NetworkUser.scoreCount by requireReadonlyProperty(intPropertyMapper("scoreStats", "score"))

val NetworkUser.votesCount by requireReadonlyProperty(intPropertyMapper("scoreStats", "votesCount"))

//val birthday: Any, // null
//val gender: String, // 0
//val location: Any, // null
//val ratingPos: Any, // null
//val relatedData: Any, // null
//val workplace: List<Any>