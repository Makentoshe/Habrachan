import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test

class NativeGetUserManagerTest: UnitTest() {

    @Test
    fun networkSuccess() = runBlocking {
        val token = ""
        val userSession = userSession("", "", token)
        val manager = com.makentoshe.habrachan.network.NativeGetUserManager.Builder(OkHttpClient()).build()
        val request = manager.request(userSession, "Makentoshe")
        val response = manager.user(request)
        println(response)
    }
}