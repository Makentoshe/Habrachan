import com.makentoshe.habrachan.network.NativeLoginManager
import com.makentoshe.habrachan.network.NativeLoginResponseException
import com.makentoshe.habrachan.network.deserializer.NativeGetMeDeserializer
import com.makentoshe.habrachan.network.manager.NativeGetMeManager
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class NativeLoginManagerTest : UnitTest() {

    @Test
    @Ignore("proper email and password should be provided")
    fun networkSuccess() = runBlocking {
        val meManager = NativeGetMeManager.Builder(OkHttpClient(), NativeGetMeDeserializer()).build()
        val manager = NativeLoginManager.Builder(OkHttpClient(), meManager).build()
        val request = manager.request(userSession, "", "")
        val response = manager.login(request)
    }

    @Test
    fun networkFailureInvalidMail() = runBlocking {
        val meManager = NativeGetMeManager.Builder(OkHttpClient(), NativeGetMeDeserializer()).build()
        val manager = NativeLoginManager.Builder(OkHttpClient(), meManager).build()
        val request = manager.request(userSession, "a@b.com", "123")
        val response = manager.login(request)

        val exception = response.exceptionOrNull() as? NativeLoginResponseException ?: throw IllegalStateException()
        assertEquals(400, exception.code)
    }

    @Test
    fun networkFailureMissingMail() = runBlocking {
        val meManager = NativeGetMeManager.Builder(OkHttpClient(), NativeGetMeDeserializer()).build()
        val manager = NativeLoginManager.Builder(OkHttpClient(), meManager).build()
        val request = manager.request(userSession, "", "123")
        val response = manager.login(request)

        val exception = response.exceptionOrNull() as? NativeLoginResponseException ?: throw IllegalStateException()
        assertEquals(400, exception.code)
    }

    @Test
    fun networkFailureMissingPassword() = runBlocking {
        val meManager = NativeGetMeManager.Builder(OkHttpClient(), NativeGetMeDeserializer()).build()
        val manager = NativeLoginManager.Builder(OkHttpClient(), meManager).build()
        val request = manager.request(userSession, "my@mail.com", "")
        val response = manager.login(request)

        val exception = response.exceptionOrNull() as? NativeLoginResponseException ?: throw IllegalStateException()
        assertEquals(400, exception.code)
    }
}