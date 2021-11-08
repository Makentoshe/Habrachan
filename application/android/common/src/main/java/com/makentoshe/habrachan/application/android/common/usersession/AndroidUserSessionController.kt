package com.makentoshe.habrachan.application.android.common.usersession

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.core.util.Supplier
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.encryption.AesValueEncryption
import com.ironz.binaryprefs.encryption.XorKeyEncryption
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.Require2
import com.makentoshe.habrachan.functional.toOption2
import com.makentoshe.habrachan.functional.toRequire2
import java.io.File
import javax.inject.Provider
import kotlin.random.Random

class AndroidUserSessionEncryption(
    val filename: String,
    val mainKeyAlias: String,
    val scheme: EncryptedFile.FileEncryptionScheme,
)

interface AndroidUserSessionProvider : Provider<AndroidUserSession?>, Supplier<AndroidUserSession?>

abstract class AndroidUserSessionController :
    Consumer<AndroidUserSessionController.AndroidUserSessionControllerApply.() -> Unit>, AndroidUserSessionProvider {

    companion object {
        private const val CLIENT_API = "client api"
        private const val CLIENT_ID = "client id"
        private const val TOKEN = "token"
        private const val HABR_SESSION_ID = "habr session id cookie"
    }

    protected abstract val sharedPreferences: SharedPreferences

    @SuppressLint("CommitPrefEdits") // We can safely ignore it, because linter doesn't understand
    private fun put(androidUserSession: AndroidUserSession) {
        with(sharedPreferences.edit()) {
            this.putString(CLIENT_API, androidUserSession.api.nullableValue?.string)
            this.putString(CLIENT_ID, androidUserSession.client.nullableValue?.string)
            this.putString(TOKEN, androidUserSession.accessToken.nullableValue?.string)
            this.putString(HABR_SESSION_ID, androidUserSession.habrSessionId.nullableValue?.string)
        }.commit()
    }

    override fun get(): AndroidUserSession? {
        val api = sharedPreferences.getString(CLIENT_API, null)?.let(::ClientApi)?.toRequire2() ?: return null
        val client = sharedPreferences.getString(CLIENT_ID, null)?.let(::ClientId)?.toRequire2() ?: return null
        val accessToken = sharedPreferences.getString(TOKEN, null)?.let(::AccessToken).toOption2()
        val habrSessionId = sharedPreferences.getString(HABR_SESSION_ID, null)?.let(::HabrSessionIdCookie).toOption2()
        return AndroidUserSession(client, api, accessToken, habrSessionId)
    }

    override fun accept(t: (AndroidUserSessionControllerApply.() -> Unit)) {
        put(AndroidUserSessionControllerApply(get()).also(t).apply())
    }

    data class AndroidUserSessionControllerApply internal constructor(val old: AndroidUserSession?) {
        var client: Require2<ClientId> = old?.client ?: Require2(null)
        var api: Require2<ClientApi> = old?.api ?: Require2(null)
        var accessToken: Option2<AccessToken> = old?.accessToken ?: Option2.None
        var habrSessionId: Option2<HabrSessionIdCookie> = old?.habrSessionId ?: Option2.None

        fun apply() = AndroidUserSession(client, api, accessToken, habrSessionId)
    }
}

@RequiresApi(api = Build.VERSION_CODES.M)
class EncryptedAndroidUserSessionController(
    context: Context,
    encryption: AndroidUserSessionEncryption
) : AndroidUserSessionController() {

    private val file = File(ContextCompat.getDataDir(context), encryption.filename)
    private val masterKey = MasterKey.Builder(context, encryption.mainKeyAlias).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    override val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        encryption.filename,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

class BinaryAndroidUserSessionController(
    context: Context,
    encryption: AndroidUserSessionEncryption
) : AndroidUserSessionController() {

    private val mainKeyAliasBytes = mainKeyAliasBytes(encryption)
    private val xorKeyEncryption = XorKeyEncryption(mainKeyAliasBytes)
    private val vectorBytes = Random(encryption.filename.hashCode()).nextBytes(16).copyOfRange(0, 16)
    private val aesValueEncryption = AesValueEncryption(mainKeyAliasBytes, vectorBytes)

    override val sharedPreferences = BinaryPreferencesBuilder(context).name(encryption.filename)
        .keyEncryption(xorKeyEncryption).valueEncryption(aesValueEncryption).build()

    private fun mainKeyAliasBytes(encryption: AndroidUserSessionEncryption): ByteArray {
        return Random(encryption.mainKeyAlias.hashCode()).nextBytes(16)
    }
}
