package com.makentoshe.habrachan.api.android.login

import com.makentoshe.habrachan.api.android.login.entity.ClientId
import com.makentoshe.habrachan.api.android.login.entity.ClientSecret
import com.makentoshe.habrachan.api.android.login.entity.GrantType
import com.makentoshe.habrachan.api.login.LoginAuthBuilder
import com.makentoshe.habrachan.api.login.entity.Email
import com.makentoshe.habrachan.api.login.entity.Password
import com.makentoshe.habrachan.api.login.requireReadWriteProperty

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

var LoginAuthBuilder.clientSecret by requireReadWriteProperty(
    key = "client_secret",
    readMap = { string -> ClientSecret(string) },
    writeMap = { clientSecret -> clientSecret.string }
)

var LoginAuthBuilder.clientId by requireReadWriteProperty(
    key = "client_id",
    readMap = { string -> ClientId(string) },
    writeMap = { clientId -> clientId.string }
)

var LoginAuthBuilder.grantType by requireReadWriteProperty(
    key = "grant_type",
    readMap = { string -> GrantType(string) },
    writeMap = { grantType -> grantType.string }
)
