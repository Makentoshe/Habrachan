package com.makentoshe.habrachan.entity.user

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.entity.user.component.UserAvatar
import com.makentoshe.habrachan.entity.user.component.UserFullName
import com.makentoshe.habrachan.entity.user.component.UserId
import com.makentoshe.habrachan.entity.user.component.UserLogin
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.Require2
import kotlinx.serialization.json.JsonElement

data class Me(
    override val parameters: Map<String, JsonElement>,
    val delegate: MePropertiesDelegate
) : AnyWithVolumeParameters<JsonElement>

interface MePropertiesDelegate {
    val id: Require2<UserId>
    val login: Require2<UserLogin>
    val fullname: Require2<UserFullName>
    val avatar: Option2<UserAvatar>
}

val Me.userid get() = delegate.id

val Me.login get() = delegate.login

val Me.fullname get() = delegate.fullname

val Me.avatar get() = delegate.avatar
