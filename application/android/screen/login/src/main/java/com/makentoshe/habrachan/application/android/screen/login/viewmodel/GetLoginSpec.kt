package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import com.makentoshe.habrachan.api.login.entity.Email
import com.makentoshe.habrachan.api.login.entity.Password

data class GetLoginSpec(val email: Email, val password: Password)