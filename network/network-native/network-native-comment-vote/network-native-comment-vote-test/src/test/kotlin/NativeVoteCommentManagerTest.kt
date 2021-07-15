import com.makentoshe.habrachan.network.NativeVoteCommentManager
import com.makentoshe.habrachan.network.request.CommentVote
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test

class NativeVoteCommentManagerTest : UnitTest() {

    @Test
    fun networkSuccess() = runBlocking {
        val userSession = userSession(client, api, token)
        val manager = NativeVoteCommentManager.Builder(OkHttpClient()).build()
        val request = manager.request(userSession, 23263444, CommentVote.Up)
        val response = manager.vote(request)

        println(response)
    }
}