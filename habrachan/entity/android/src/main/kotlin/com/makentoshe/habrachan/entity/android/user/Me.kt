package com.makentoshe.habrachan.entity.android.user

//data class MePropertiesDelegateImpl(
//    override val parameters: Map<String, JsonElement>,
//) : MePropertiesDelegate, AnyWithVolumeParameters<JsonElement> {
//
//    override val id by requireReadonlyProperty(propertyMapper("id") { jsonElement ->
//        UserId(jsonElement.jsonPrimitive.int)
//    })
//
//    override val login by requireReadonlyProperty(propertyMapper("login") { jsonElement ->
//        UserLogin(jsonElement.jsonPrimitive.content)
//    })
//
//    override val fullname by requireReadonlyProperty(propertyMapper("fullname") { jsonElement ->
//        UserFullName(jsonElement.jsonPrimitive.content)
//    })
//
//    override val avatar by optionReadonlyProperty("avatar") { jsonElement ->
//        UserAvatar(jsonElement.jsonPrimitive.content)
//    }
//}
//
//val Me.specialism by optionReadonlyProperty(stringPropertyMapper("specializm"))
//
//val Me.score by optionReadonlyProperty(intPropertyMapper("specializm"))
//
//val Me.ratingPosition by optionReadonlyProperty(stringPropertyMapper("rating_position"))
//
//val Me.rating by optionReadonlyProperty(floatPropertyMapper("rating"))
//
//val Me.isReadonly by optionReadonlyProperty(booleanPropertyMapper("is_readonly"))
//
//val Me.isRc by optionReadonlyProperty(booleanPropertyMapper("is_rc"))
//
//val Me.isSubscribed by optionReadonlyProperty(booleanPropertyMapper("is_subscribed"))
//
//val Me.isCanVote by optionReadonlyProperty(booleanPropertyMapper("is_can_vote"))
