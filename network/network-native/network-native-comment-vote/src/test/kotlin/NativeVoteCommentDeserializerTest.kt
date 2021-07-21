import com.makentoshe.habrachan.entity.CommentVote
import com.makentoshe.habrachan.entity.commentId
import com.makentoshe.habrachan.network.NativeVoteCommentDeserializer
import com.makentoshe.habrachan.network.NativeVoteCommentException
import com.makentoshe.habrachan.network.NativeVoteCommentRequest
import org.junit.Assert.assertEquals
import org.junit.Test

class NativeVoteCommentDeserializerTest : UnitTest() {

    @Test
    fun testShouldHandleJsonParsingErrorsForSuccessResponse() {
        val failureJson = "vote_comment_failure.json"
        val code = 0
        val message = "Random message"
        val request = NativeVoteCommentRequest(userSession, commentId(-1), CommentVote.Down)
        val deserializer = NativeVoteCommentDeserializer()

        val response = deserializer.success(request, failureJson, code, message).exceptionOrNull() as NativeVoteCommentException
        assertEquals(request, response.request)
        assertEquals(failureJson, response.raw)
        assertEquals(code, response.code)
        assertEquals(message, response.message)
        assertEquals(1, response.additional.size)
    }

    @Test
    fun testShouldHandleJsonParsingErrorsForFailureResponse() {
        val failureJson = "vote_comment_failure.json"
        val code = 0
        val message = "Random message"
        val request = NativeVoteCommentRequest(userSession, commentId(-1), CommentVote.Down)
        val deserializer = NativeVoteCommentDeserializer()

        val response = deserializer.failure(request, failureJson, code, message).exceptionOrNull() as NativeVoteCommentException
        assertEquals(request, response.request)
        assertEquals(failureJson, response.raw)
        assertEquals(code, response.code)
        assertEquals(message, response.message)
        assertEquals(1, response.additional.size)
    }
}