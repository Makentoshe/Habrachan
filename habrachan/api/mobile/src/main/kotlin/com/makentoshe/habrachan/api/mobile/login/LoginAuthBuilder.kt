package com.makentoshe.habrachan.api.mobile.login

import com.makentoshe.habrachan.api.login.LoginAuthBuilder
import com.makentoshe.habrachan.api.login.entity.Email
import com.makentoshe.habrachan.api.login.entity.Password
import com.makentoshe.habrachan.api.login.requireReadWriteProperty
import com.makentoshe.habrachan.api.mobile.login.entity.*

var LoginAuthBuilder.password by requireReadWriteProperty(
    key = "password",
    readMap = { string -> Password(string) },
    writeMap = { password -> password.string }
)

var LoginAuthBuilder.email by requireReadWriteProperty(
    key = "email",
    readMap = { string -> Email(string) },
    writeMap = { email -> email.string }
)

var LoginAuthBuilder.state by requireReadWriteProperty(
    key = "state",
    readMap = { string -> State(string) },
    writeMap = { clientSecret -> clientSecret.string }
)

var LoginAuthBuilder.consumer by requireReadWriteProperty(
    key = "consumer",
    readMap = { string -> Consumer(string) },
    writeMap = { clientId -> clientId.string }
)

var LoginAuthBuilder.captcha by requireReadWriteProperty(
    key = "captcha",
    readMap = { string -> Captcha(string) },
    writeMap = { grantType -> grantType.string }
)

var LoginAuthBuilder.googleRecaptchaResponse by requireReadWriteProperty(
    key = "g-recaptcha-response",
    readMap = { string -> GRecaptchaResponse(string) },
    writeMap = { grantType -> grantType.string }
)

var LoginAuthBuilder.captchaType by requireReadWriteProperty(
    key = "captcha_type",
    readMap = { string -> CaptchaType(string) },
    writeMap = { grantType -> grantType.string }
)
