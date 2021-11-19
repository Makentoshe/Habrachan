package com.makentoshe.habrachan.application.common.arena.user.get

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.entity.user.component.UserLogin

class GetUserArenaRequest(val login: UserLogin, val parameters: AdditionalRequestParameters)