import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.user.get.*
import com.makentoshe.habrachan.entity.user.component.UserLogin
import com.makentoshe.habrachan.entity.user.get.NetworkUser
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.user.get.GetUserException
import com.makentoshe.habrachan.network.user.get.GetUserManager
import com.makentoshe.habrachan.network.user.get.GetUserRequest
import com.makentoshe.habrachan.network.user.get.GetUserResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert

abstract class UsersArenaTest {

    protected fun `build arena storage`(
        response: Either2<GetUserArenaResponse, ArenaStorageException>,
    ): ArenaCache3<GetUserArenaRequest, GetUserArenaResponse> {
        val mockArenaStorage = mockk<ArenaCache3<GetUserArenaRequest, GetUserArenaResponse>>(relaxed = true)
        every { mockArenaStorage.fetch(any()) } returns response
        return mockArenaStorage
    }

    protected fun `build factory`(
        `with manager`: GetUserManager = `build user manager`(Either2.Right(GetUserException(mockk(), null))),
        `with storage`: ArenaCache3<GetUserArenaRequest, GetUserArenaResponse> = `build arena storage`(
            Either2.Right(ArenaStorageException("Stub"))
        ),
    ): UsersArena.Factory {
        return UsersArena.Factory(`with manager`, `with storage`)
    }

    protected fun `build user manager`(
        response: Either2<GetUserResponse, GetUserException>,
    ): GetUserManager {
        val mockGetUserManager = mockk<GetUserManager>()
        coEvery { mockGetUserManager.execute(any()) } returns response
        return mockGetUserManager
    }

    protected fun `custom network user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = NetworkUser(parameters)

    protected fun `custom arena user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = UserFromArena(parameters)

    protected fun `build manager response`(`with user`: NetworkUser): GetUserResponse {
        val parameters = AdditionalRequestParameters()
        return GetUserResponse(GetUserRequest(UserLogin("sas"), parameters), `with user`)
    }

    protected fun `build storage response`(`with user`: UserFromArena): GetUserArenaResponse {
        val parameters = AdditionalRequestParameters()
        return GetUserArenaResponse(GetUserArenaRequest(UserLogin("sas"), parameters), `with user`)
    }

    protected infix fun UserFromArena?.`login should be`(login: String) {
        Assert.assertEquals("Login value should be $login", login, this?.login?.nullableValue?.string)
    }
}