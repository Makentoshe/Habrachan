package com.makentoshe.habrachan.application.android.di

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.makentoshe.habrachan.application.android.common.BuildVersionProvider
import com.makentoshe.habrachan.application.android.common.usersession.*
import toothpick.config.Module
import toothpick.ktp.binding.bind
import kotlin.random.Random

class UserModule(context: Context, private val buildVersionProvider: BuildVersionProvider) : Module() {

    init {
        val controller = buildController(context)
        bind<AndroidUserSessionProvider>().toInstance(controller)
        bind<AndroidUserSessionController>().toInstance(controller)
        bind<AndroidUserSessionController>().toInstance(controller)
    }

    @SuppressLint("NewApi")// Api checking with buildVersionProvider
    private fun buildController(context: Context): AndroidUserSessionController {
        val scheme = EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        val encryption = AndroidUserSessionEncryption("filename", buildAlias(context), scheme)
        return if (buildVersionProvider.is23orAbove()) {
            EncryptedAndroidUserSessionController(context, encryption)
        } else {
            BinaryAndroidUserSessionController(context, encryption)
        }
    }

    @SuppressLint("HardwareIds")// require only for 21 < api < 23
    private fun buildAlias(context: Context): String = if (buildVersionProvider.is23orAbove()) {
        MasterKey.DEFAULT_MASTER_KEY_ALIAS
    } else {
        val seed = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID).hashCode()
        Random(seed).nextBytes(16).decodeToString()
    }
}
