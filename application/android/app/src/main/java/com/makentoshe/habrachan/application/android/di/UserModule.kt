package com.makentoshe.habrachan.application.android.di

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEventAddrless
import com.makentoshe.habrachan.application.android.common.BuildVersionProvider
import com.makentoshe.habrachan.application.android.common.usersession.*
import com.makentoshe.habrachan.functional.toRequire2
import toothpick.config.Module
import toothpick.ktp.binding.bind
import kotlin.random.Random

class UserModule(context: Context, private val buildVersionProvider: BuildVersionProvider) : Module() {

    companion object : Analytics(LogAnalytic())

    init {
        val controller = buildController(context).also(::installController)
        bind<AndroidUserSessionProvider>().toInstance(controller)
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

    private fun installController(controller: AndroidUserSessionController) = controller.accept {
        // if requireable params weren't installed, the old value will be null
        if (old != null) return@accept capture(analyticEventAddrless { "AndroidUserSession was already installed" })

        this.client = ClientId(BuildConfig.CLIENT_KEY).toRequire2()
        this.api = ClientApi(BuildConfig.API_KEY).toRequire2()

        capture(analyticEventAddrless {
            "Install AndroidUserSession(client=${this@accept.client.nullableValue}, api=${this@accept.api.nullableValue}"
        })
    }
}
