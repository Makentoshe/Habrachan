import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArena
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaRequest
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaResponse
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserFromArena
import com.makentoshe.habrachan.entity.me.mobile.NetworkMeUser
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.left
import com.makentoshe.habrachan.functional.rightOrNull
import com.makentoshe.habrachan.network.me.mobile.MeUserException
import com.makentoshe.habrachan.network.me.mobile.MeUserManager
import com.makentoshe.habrachan.network.me.mobile.MeUserRequest
import com.makentoshe.habrachan.network.me.mobile.MeUserResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.*
import org.junit.Test

class MeUserArenaTest {

    @Test
    fun `test should check load from cache firstly and continue loading from source`() = runBlocking {
        val expectedResponse = `build storage response`(`custom arena user`())
        val storage = `build arena storage`(Either2.Left(expectedResponse))

        val meUserArena = `build arena`(`with storage` = storage)
        val flowArenaResponse = meUserArena.suspendFlowFetch(mockk(relaxed = true)).first()

        assertTrue(flowArenaResponse.loading)
    }

    @Test
    fun `test should check successfully load from cache firstly`() = runBlocking {
        val expectedResponse = `build storage response`(`custom arena user`())
        val storage = `build arena storage`(Either2.Left(expectedResponse))

        val meUserArena = `build arena`(`with storage` = storage)
        val flowArenaResponse = meUserArena.suspendFlowFetch(mockk(relaxed = true)).first()

        // is successful content and is expected content returned
        assertTrue(flowArenaResponse.content.isLeft())
        assertEquals(expectedResponse, flowArenaResponse.content.left())
    }

    @Test
    fun `test should check failure load from cache firstly`() = runBlocking {
        val storage = `build arena storage`(Either2.Right(ArenaStorageException("Stub")))

        val meUserArena = `build arena`(`with storage` = storage)
        val flowArenaResponse = meUserArena.suspendFlowFetch(mockk(relaxed = true)).first()

        // is failure content and is expected content wasn't returned
        assertTrue(flowArenaResponse.content.isRight())
        assertTrue(flowArenaResponse.content.rightOrNull()?.cacheException is ArenaStorageException)
    }

    @Test
    fun `test should check load from source after cache and stop loading`() = runBlocking {
        val expectedResponse = `build manager response`(`custom network user`())
        val manager = `build user manager`(Either2.Left(expectedResponse))

        val meUserArena = `build arena`(`with manager` = manager)
        val flowArenaResponse = meUserArena.suspendFlowFetch(mockk(relaxed = true)).drop(1).first()

        assertFalse(flowArenaResponse.loading)
    }

    @Test
    fun `test should check successfully load from source`() = runBlocking {
        val expectedResponse = `build manager response`(`custom network user`())
        val manager = `build user manager`(Either2.Left(expectedResponse))

        val meUserArena = `build arena`(`with manager` = manager)
        val flowArenaResponse = meUserArena.suspendFlowFetch(mockk(relaxed = true)).drop(1).first()

        // is successful content and the same content returned
        assertTrue(flowArenaResponse.content.isLeft())
        assertEquals(expectedResponse.me.parameters, flowArenaResponse.content.left().me.parameters)
    }

    @Test
    fun `test should check failure load from source`() = runBlocking {
        val manager = `build user manager`(Either2.Right(MeUserException(mockk())))

        val meUserArena = `build arena`(`with manager` = manager)
        val flowArenaResponse = meUserArena.suspendFlowFetch(mockk(relaxed = true)).drop(1).first()

        // is failure content and expected exception returned
        assertTrue(flowArenaResponse.content.isRight())
        assertTrue(flowArenaResponse.content.rightOrNull()?.sourceException is MeUserException)
    }


    private fun `build arena`(
        `with manager`: MeUserManager = `build user manager`(Either2.Right(MeUserException(mockk(), null))),
        `with storage`: ArenaCache3<MeUserArenaRequest, MeUserArenaResponse> = `build arena storage`(
            Either2.Right(ArenaStorageException("Stub"))
        ),
    ): MeUserArena {
        return MeUserArena(`with manager`, `with storage`)
    }

    private fun `build arena storage`(
        response: Either2<MeUserArenaResponse, ArenaStorageException>,
    ): ArenaCache3<MeUserArenaRequest, MeUserArenaResponse> {
        val mockArenaStorage = mockk<ArenaCache3<MeUserArenaRequest, MeUserArenaResponse>>(relaxed = true)
        every { mockArenaStorage.fetch(any()) } returns response
        return mockArenaStorage
    }

    private fun `build user manager`(
        response: Either2<MeUserResponse, MeUserException>,
    ): MeUserManager {
        val mockMeUserManager = mockk<MeUserManager>()
        coEvery { mockMeUserManager.execute(any()) } returns response
        return mockMeUserManager
    }

    private fun `custom network user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = NetworkMeUser(parameters)

    private fun `custom arena user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = MeUserFromArena(parameters)

    private fun `build manager response`(`with user`: NetworkMeUser): MeUserResponse {
        val parameters = AdditionalRequestParameters()
        return MeUserResponse(MeUserRequest(parameters), `with user`)
    }

    private fun `build storage response`(`with user`: MeUserFromArena): MeUserArenaResponse {
        val parameters = AdditionalRequestParameters()
        return MeUserArenaResponse(MeUserArenaRequest(parameters), `with user`)
    }
}