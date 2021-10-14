package com.makentoshe.habrachan.api.android.login

import com.makentoshe.habrachan.api.android.login.entity.ClientId
import com.makentoshe.habrachan.api.android.login.entity.ClientSecret
import com.makentoshe.habrachan.api.android.login.entity.GrantType
import com.makentoshe.habrachan.api.login.LoginAuth
import com.makentoshe.habrachan.api.login.entity.Email
import com.makentoshe.habrachan.api.login.entity.Password
import com.makentoshe.habrachan.api.login.requireReadonlyProperty

val LoginAuth.password by requireReadonlyProperty(
    key = "password",
    map = { string -> Password(string) }
)

val LoginAuth.email by requireReadonlyProperty(
    key = "email",
    map = { string -> Email(string) },
)

val LoginAuth.grantType by requireReadonlyProperty(
    key = "grant_type",
    map = { string -> GrantType(string) },
)

val LoginAuth.clientId by requireReadonlyProperty(
    key = "client_id",
    map = { string -> ClientId(string) },
)

val LoginAuth.clientSecret by requireReadonlyProperty(
    key = "client_secret",
    map = { string -> ClientSecret(string) },
)