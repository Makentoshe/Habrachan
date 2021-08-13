import com.makentoshe.habrachan.network.NativeGetArticleCommentsManager
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class NativeGetArticleCommentsManagerTest {

    @Test
    @Ignore
    fun networkSuccess() = runBlocking {
        val userSession = userSession("", "")
        val manager = NativeGetArticleCommentsManager.Builder(OkHttpClient()).build()
        val request = manager.request(userSession, 442440)
        val response = manager.comments(request)
        println(response)
    }
}