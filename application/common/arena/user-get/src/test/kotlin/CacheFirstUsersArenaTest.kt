import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArenaResponse
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.leftOrNull
import com.makentoshe.habrachan.functional.rightOrNull
import com.makentoshe.habrachan.network.user.get.GetUserResponse
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CacheFirstUsersArenaTest : UsersArenaTest() {

    @Test
    fun `test should load from cache firstly`() = runBlocking {
        val expectedResponse = mockk<GetUserArenaResponse>()
        val storage = `build arena storage`(Either2.Left(expectedResponse))

        val factory = `build factory`(`with storage` = storage)
        val actualResponse = factory.cacheFirstArena().suspendFetch(mockk(relaxed = true)).leftOrNull()

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `test should load from source if cache fails`() = runBlocking {
        val expectedResponse = mockk<GetUserResponse>(relaxed = true)
        val manager = `build user manager`(Either2.Left(expectedResponse))

        val factory = `build factory`(`with manager` = manager)
        val actualResponse = factory.cacheFirstArena().suspendFetch(mockk(relaxed = true)).leftOrNull()

        assertEquals(expectedResponse.request.login, actualResponse?.request?.login)
        assertEquals(expectedResponse.user.parameters, actualResponse?.user?.parameters)
    }

    @Test
    fun `test should carry source if cache fails`() = runBlocking {
        val storage = `build arena storage`(Either2.Right(ArenaStorageException("Stub")))
        val manager = `build user manager`(Either2.Left(mockk(relaxed = true)))

        val factory = `build factory`(`with manager` = manager, `with storage` = storage)
        factory.cacheFirstArena().suspendFetch(mockk(relaxed = true)).leftOrNull()

        verify { storage.carry(any(), any()) }
    }

    @Test
    fun `test should check arena returns exception`() = runBlocking {
        val either = `build factory`().cacheFirstArena().suspendFetch(mockk(relaxed = true))

        assertTrue(either.isRight())
        assertTrue(either.rightOrNull() is ArenaException)
    }

}