package com.makentoshe.habrachan.api.mobile.login

import com.makentoshe.habrachan.api.login.LoginAuth
import com.makentoshe.habrachan.api.login.entity.Email
import com.makentoshe.habrachan.api.login.entity.Password
import com.makentoshe.habrachan.api.login.requireReadonlyProperty
import com.makentoshe.habrachan.api.mobile.login.entity.*

val LoginAuth.password by requireReadonlyProperty(
    key = "password",
    map = { string -> Password(string) }
)

val LoginAuth.email by requireReadonlyProperty(
    key = "email",
    map = { string -> Email(string) },
)

val LoginAuth.state by requireReadonlyProperty(
    key = "email",
    map = { string -> State(string) },
)

val LoginAuth.consumer by requireReadonlyProperty(
    key = "consumer",
    map = { string -> Consumer(string) },
)

val LoginAuth.captcha by requireReadonlyProperty(
    key = "captcha",
    map = { string -> Captcha(string) },
)

val LoginAuth.googleRecaptchaResponse by requireReadonlyProperty(
    key = "g-recaptcha-response",
    map = { string -> GRecaptchaResponse(string) },
)

val LoginAuth.captchaType by requireReadonlyProperty(
    key = "captcha_type",
    map = { string -> CaptchaType(string) },
)
