import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.user.me.*
import com.makentoshe.habrachan.entity.me.mobile.NetworkMeUser
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.me.mobile.MeUserException
import com.makentoshe.habrachan.network.me.mobile.MeUserManager
import com.makentoshe.habrachan.network.me.mobile.MeUserRequest
import com.makentoshe.habrachan.network.me.mobile.MeUserResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert

abstract class MeUserArenaTest {

    protected fun `build arena storage`(
        response: Either2<MeUserArenaResponse, ArenaStorageException>,
    ): ArenaCache3<MeUserArenaRequest, MeUserArenaResponse> {
        val mockArenaStorage = mockk<ArenaCache3<MeUserArenaRequest, MeUserArenaResponse>>(relaxed = true)
        every { mockArenaStorage.fetch(any()) } returns response
        return mockArenaStorage
    }

    protected fun `build factory`(
        `with manager`: MeUserManager = `build user manager`(Either2.Right(MeUserException(mockk(), null))),
        `with storage`: ArenaCache3<MeUserArenaRequest, MeUserArenaResponse> = `build arena storage`(
            Either2.Right(ArenaStorageException("Stub"))
        ),
    ): MeUserArena.Factory {
        return MeUserArena.Factory(`with manager`, `with storage`)
    }

    protected fun `build user manager`(
        response: Either2<MeUserResponse, MeUserException>,
    ): MeUserManager {
        val mockMeUserManager = mockk<MeUserManager>()
        coEvery { mockMeUserManager.execute(any()) } returns response
        return mockMeUserManager
    }

    protected fun `custom network user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = NetworkMeUser(parameters)

    protected fun `custom arena user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = MeUserFromArena(parameters)

    protected fun `build manager response`(`with user`: NetworkMeUser): MeUserResponse {
        val parameters = AdditionalRequestParameters()
        return MeUserResponse(MeUserRequest(parameters), `with user`)
    }

    protected fun `build storage response`(`with user`: MeUserFromArena): MeUserArenaResponse {
        val parameters = AdditionalRequestParameters()
        return MeUserArenaResponse(MeUserArenaRequest(parameters), `with user`)
    }

    protected infix fun MeUserFromArena?.`login should be`(login: String) {
        Assert.assertEquals("Login value should be $login", login, this?.login?.nullableValue?.string)
    }
}