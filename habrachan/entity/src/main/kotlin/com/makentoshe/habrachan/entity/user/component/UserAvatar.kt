package com.makentoshe.habrachan.entity.user.component

/**
 * Declares an url to avatar image. This value should not be empty.
 * If avatar value might be empty, just wrap this type with Option
 * and return Option.None instead of empty string.
 * */
@JvmInline
value class UserAvatar(val string: String)


