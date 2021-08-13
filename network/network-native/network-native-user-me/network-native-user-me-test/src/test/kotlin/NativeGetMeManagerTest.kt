import com.makentoshe.habrachan.network.NativeGetMeManager
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test

class NativeGetMeManagerTest : UnitTest() {

    private val manager = NativeGetMeManager.Builder(OkHttpClient()).build()

    @Test
    fun success() = runBlocking {
        val session = userSession(client, api, token)
        val request = manager.request(session)
        val response = manager.me(request)

        println(response)
    }

    @Test
    fun failure() = runBlocking {
        val session = userSession(client, api, "mailformed token")
        val request = manager.request(session)
        val response = manager.me(request)

        println(response)
    }
}