import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArena
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArenaRequest
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArenaResponse
import com.makentoshe.habrachan.application.common.arena.user.get.UserFromArena
import com.makentoshe.habrachan.entity.user.component.UserLogin
import com.makentoshe.habrachan.entity.user.get.NetworkUser
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.left
import com.makentoshe.habrachan.functional.rightOrNull
import com.makentoshe.habrachan.network.user.get.GetUserException
import com.makentoshe.habrachan.network.user.get.GetUserManager
import com.makentoshe.habrachan.network.user.get.GetUserRequest
import com.makentoshe.habrachan.network.user.get.GetUserResponse
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

class GetUserArenaTest {

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
        assertEquals(expectedResponse.user.parameters, flowArenaResponse.content.left().user.parameters)
    }

    @Test
    fun `test should check failure load from source`() = runBlocking {
        val manager = `build user manager`(Either2.Right(GetUserException(mockk(), null)))

        val meUserArena = `build arena`(`with manager` = manager)
        val flowArenaResponse = meUserArena.suspendFlowFetch(mockk(relaxed = true)).drop(1).first()

        // is failure content and expected exception returned
        assertTrue(flowArenaResponse.content.isRight())
        assertTrue(flowArenaResponse.content.rightOrNull()?.sourceException is GetUserException)
    }


    private fun `build arena`(
        `with manager`: GetUserManager = `build user manager`(Either2.Right(GetUserException(mockk(), null))),
        `with storage`: ArenaCache3<GetUserArenaRequest, GetUserArenaResponse> = `build arena storage`(
            Either2.Right(ArenaStorageException("Stub"))
        ),
    ): GetUserArena {
        return GetUserArena(`with manager`, `with storage`)
    }

    private fun `build arena storage`(
        response: Either2<GetUserArenaResponse, ArenaStorageException>,
    ): ArenaCache3<GetUserArenaRequest, GetUserArenaResponse> {
        val mockArenaStorage = mockk<ArenaCache3<GetUserArenaRequest, GetUserArenaResponse>>(relaxed = true)
        every { mockArenaStorage.fetch(any()) } returns response
        return mockArenaStorage
    }

    private fun `build user manager`(
        response: Either2<GetUserResponse, GetUserException>,
    ): GetUserManager {
        val mockMeUserManager = mockk<GetUserManager>()
        coEvery { mockMeUserManager.execute(any()) } returns response
        return mockMeUserManager
    }

    private fun `custom network user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = NetworkUser(parameters)

    private fun `custom arena user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = UserFromArena(parameters)

    private fun `build manager response`(`with user`: NetworkUser): GetUserResponse {
        val parameters = AdditionalRequestParameters()
        return GetUserResponse(GetUserRequest(UserLogin("Makentoshe"), parameters), `with user`)
    }

    private fun `build storage response`(`with user`: UserFromArena): GetUserArenaResponse {
        val parameters = AdditionalRequestParameters()
        return GetUserArenaResponse(GetUserArenaRequest(UserLogin("Makentoshe"), parameters), `with user`)
    }
}