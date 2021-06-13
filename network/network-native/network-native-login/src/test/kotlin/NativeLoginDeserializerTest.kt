import com.makentoshe.habrachan.network.NativeLoginDeserializer
import com.makentoshe.habrachan.network.NativeLoginRequest
import com.makentoshe.habrachan.network.NativeLoginResponseException
import org.junit.Assert.assertEquals
import org.junit.Test

class NativeLoginDeserializerTest: UnitTest() {

    private val deserializer = NativeLoginDeserializer()

    @Test
    fun shouldDeserializeLoginFailureWithMissingFields() {
        val request = NativeLoginRequest(userSession, "", "")
        val json = getResourceString("login_failure_1.json")
        val exception = deserializer.error(request, json).exceptionOrNull() as NativeLoginResponseException

        assertEquals(request, exception.request)
        assertEquals(json, exception.raw)
        assertEquals(400, exception.code)
        assertEquals("MISSING", exception.email)
        assertEquals("MISSING", exception.password)
        assertEquals("Bad request", exception.message)
    }

    @Test
    fun shouldDeserializeLoginFailureWithAccountError() {
        val request = NativeLoginRequest(userSession, "", "")
        val json = getResourceString("login_failure_2.json")
        val exception = deserializer.error(request, json).exceptionOrNull() as NativeLoginResponseException

        assertEquals(request, exception.request)
        assertEquals(json, exception.raw)
        assertEquals(400, exception.code)
        assertEquals("Пользователь с такой электронной почтой или паролем не найден", exception.other)
        assertEquals("Habr Account error", exception.message)
    }
}