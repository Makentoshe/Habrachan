import androidx.security.crypto.EncryptedFile
import androidx.test.core.app.ApplicationProvider
import com.makentoshe.habrachan.application.android.common.usersession.*
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.Require2
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class AndroidUserSessionControllerTest {

    @After // clear shared preferences (return them to their default state)
    fun after() {
        `create encrypted controller`(`with encryption` = `create encryption`()).accept {
            this.api = Require2(null)
            this.client = Require2(null)
            this.token = Option2.None
            this.habrSessionId = Option2.None
        }
    }

    @Test
    fun `test should return null when testfile doesn't exist`() {
        val controller = `create encrypted controller`(`with encryption` = `create encryption`())
        controller.get().`should be null`()
    }

    @Test
    fun `test should check that null returns when api param wasn't provided for storing`() {
        val controller = `create encrypted controller`(`with encryption` = `create encryption`())
        controller.accept {
//            this.api = Require2(ClientApi("client_api"))
            this.client = Require2(ClientId("client_id"))
            this.token = Option2.from(Token("token"))
            this.habrSessionId = Option2.None
        }
        controller.get().`should be null`()
    }

    @Test
    fun `test should check that null returns when client param wasn't provided for storing`() {
        val controller = `create encrypted controller`(`with encryption` = `create encryption`())
        controller.accept {
            this.api = Require2(ClientApi("client_api"))
//            this.client = Require2(ClientId("client_id"))
            this.token = Option2.from(Token("token"))
            this.habrSessionId = Option2.None
        }
        controller.get().`should be null`()
    }

    @Test
    fun `test should check that nonnull returns when token param wasn't provided for storing`() {
        val habrSessionIdOption = Option2.from(HabrSessionIdCookie("cookie"))
        val clientRequire = Require2(ClientId("client id"))
        val apiRequire = Require2(ClientApi("client api"))

        val controller = `create encrypted controller`(`with encryption` = `create encryption`())
        controller.accept {
            this.api = apiRequire
            this.client = clientRequire
//            this.token = Option2.from(Token("token"))
            this.habrSessionId = habrSessionIdOption
        }
        val androidUserSession = controller.get().`shouldn't be null`()
        androidUserSession?.client `should be` clientRequire
        androidUserSession?.api `should be` apiRequire
        androidUserSession?.token `should be` Option2.None
        androidUserSession?.habrSessionId `should be` habrSessionIdOption
    }

    @Test
    fun `test should check that nonnull returns when habr session id param wasn't provided for storing`() {
        val apiRequire = Require2(ClientApi("client api"))
        val clientRequire = Require2(ClientId("client id"))
        val tokenOption = Option2.from(Token("token"))

        val controller = `create encrypted controller`(`with encryption` = `create encryption`())
        controller.accept {
            this.api = apiRequire
            this.client = clientRequire
            this.token = tokenOption
//            this.habrSessionId = habrSessionIdOption
        }

        val androidUserSession = controller.get().`shouldn't be null`()
        androidUserSession?.client `should be` clientRequire
        androidUserSession?.api `should be` apiRequire
        androidUserSession?.token `should be` tokenOption
        androidUserSession?.habrSessionId `should be` Option2.None
    }

    @Test
    fun `test should check that second accept attempt allowed`() {
        val apiRequire = Require2(ClientApi("client api"))
        val clientRequire = Require2(ClientId("client id"))
        val tokenOption = Option2.from(Token("token"))
        val habrSessionIdOption = Option2.from(HabrSessionIdCookie("cookie"))

        val controller = `create encrypted controller`(`with encryption` = `create encryption`())
        controller.accept {
            this.api = apiRequire
            this.client = clientRequire
            this.token = tokenOption
//            this.habrSessionId = habrSessionIdOption
        }
        controller.accept {
            this.api `should be` apiRequire
            this.client `should be` clientRequire
            this.token `should be` tokenOption
            this.habrSessionId = habrSessionIdOption
        }

        val androidUserSession = controller.get().`shouldn't be null`()
        androidUserSession?.client `should be` clientRequire
        androidUserSession?.api `should be` apiRequire
        androidUserSession?.token `should be` tokenOption
        androidUserSession?.habrSessionId `should be` habrSessionIdOption
    }

    private fun Any?.`should be null`() = assertNull(this)

    private fun <T : Any?> T.`shouldn't be null`(): T = assertNotNull(this).let { return@let this }

    private infix fun <T : Any?> T.`should be`(other: T) = assertEquals(other, this)

    private fun `create encryption`(): AndroidUserSessionEncryption {
        val scheme = EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        val alias = "12345678"
        return AndroidUserSessionEncryption("testfile", alias, scheme)
    }

    protected abstract fun `create encrypted controller`(`with encryption`: AndroidUserSessionEncryption): AndroidUserSessionController
}

@RunWith(RobolectricTestRunner::class)
class EncryptedAndroidUserSessionControllerTest : AndroidUserSessionControllerTest() {

    override fun `create encrypted controller`(`with encryption`: AndroidUserSessionEncryption): EncryptedAndroidUserSessionController {
        return EncryptedAndroidUserSessionController(ApplicationProvider.getApplicationContext(), `with encryption`)
    }
}

@RunWith(RobolectricTestRunner::class)
class BinaryAndroidUserSessionControllerTest : AndroidUserSessionControllerTest() {

    override fun `create encrypted controller`(`with encryption`: AndroidUserSessionEncryption): BinaryAndroidUserSessionController {
        return BinaryAndroidUserSessionController(ApplicationProvider.getApplicationContext(), `with encryption`)
    }
}
