import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaResponse
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.leftOrNull
import com.makentoshe.habrachan.functional.rightOrNull
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class SourceFirstUsersArenaTest : MeUserArenaTest() {

    @Test
    fun `test should check loading from cache firstly`() = runBlocking {
        val managerResponse = `build manager response`(`with user` = `custom network user`())
        val manager = `build user manager`(response = Either2.Left(managerResponse))

        val sourceArena = `build factory`(`with manager` = manager).sourceFirstArena()
        val actualResponse = sourceArena.suspendFetch(mockk(relaxed = true)).leftOrNull()

        actualResponse?.me `login should be` "Makentoshe"
    }

    @Test
    fun `test should load from cache if source fails`() = runBlocking {
        val storageResponse = `build storage response`(`with user` = `custom arena user`())
        val storage = `build arena storage`(Either2.Left(storageResponse))

        val sourceArena = `build factory`(`with storage` = storage).sourceFirstArena()
        val actualResponse = sourceArena.suspendFetch(mockk(relaxed = true)).leftOrNull()

        actualResponse?.me `login should be` "Makentoshe"
        verify { storage.fetch(any()) }
    }

    @Test
    fun `test should carry source`() = runBlocking {
        val slotGetUserArenaResponse = slot<MeUserArenaResponse>()

        val managerResponse = `build manager response`(`with user` = `custom network user`())
        val manager = `build user manager`(response = Either2.Left(managerResponse))
        val storage = `build arena storage`(Either2.Right(ArenaStorageException("Stub!")))
        every { storage.carry(any(), capture(slotGetUserArenaResponse)) } just Runs

        val sourceArena = `build factory`(`with manager` = manager, `with storage` = storage).sourceFirstArena()
        sourceArena.suspendFetch(mockk(relaxed = true)).leftOrNull()

        slotGetUserArenaResponse.captured.me `login should be` "Makentoshe"
        verify { storage.carry(any(), any()) }
    }

    @Test
    fun `test should check arena returns exception`() = runBlocking {
        val either = `build factory`().cacheFirstArena().suspendFetch(mockk(relaxed = true))

        Assert.assertTrue(either.isRight())
        Assert.assertTrue(either.rightOrNull() is ArenaException)
    }

}