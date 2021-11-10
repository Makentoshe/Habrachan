package com.makentoshe.habrachan.application.android.common.usersession

import androidx.security.crypto.EncryptedFile

class AndroidUserSessionEncryption(
    val filename: String,
    val mainKeyAlias: String,
    val scheme: EncryptedFile.FileEncryptionScheme,
)