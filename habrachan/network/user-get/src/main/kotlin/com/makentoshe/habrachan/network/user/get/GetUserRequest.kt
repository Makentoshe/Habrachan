package com.makentoshe.habrachan.network.user.get

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.entity.user.component.UserLogin

data class GetUserRequest(val login: UserLogin, val parameters: AdditionalRequestParameters)