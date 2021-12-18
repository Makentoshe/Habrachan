import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.makentoshe.application.android.common.user.get.GetUserViewModel
import com.makentoshe.application.android.common.user.get.GetUserViewModelRequest
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSession
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.ClientApi
import com.makentoshe.habrachan.application.android.common.usersession.ClientId
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.application.common.arena.FlowArenaResponse
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArena
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArenaResponse
import com.makentoshe.habrachan.entity.user.component.UserLogin
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.left
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class GetUserViewModelTest {

    @get:Rule
    internal var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `test should return success model with initial option 1`() = runBlocking {
        val viewModel = `build MeUserViewModel with default responses`()

        val second = viewModel.model.first()
        assertTrue(second.content.isLeft())
        assertTrue(second.loading)
    }

    @Test
    fun `test should return success model with initial option 2`() = runBlocking {
        val viewModel = `build MeUserViewModel with default responses`()

        val second = viewModel.model.drop(1).first()
        assertTrue(second.content.isLeft())
        assertFalse(second.loading)
    }

    @Test
    fun `test should return success model with channel 1`() = runBlocking {
        val viewModel = `build MeUserViewModel with default responses`(initial = Option2.None)

        val viewModelRequest = GetUserViewModelRequest(UserLogin("Makentoshe"))
        launch { viewModel.channel.send(viewModelRequest) }

        val first = viewModel.model.first()
        assertEquals(viewModelRequest, first.content.left().request)
        assertTrue(first.loading)
    }

    @Test
    fun `test should return success model with channel 2`() = runBlocking {
        val viewModel = `build MeUserViewModel with default responses`(initial = Option2.None)

        val viewModelRequest = GetUserViewModelRequest(UserLogin("Makentoshe"))
        launch { viewModel.channel.send(viewModelRequest) }

        val second = viewModel.model.drop(1).first()
        assertEquals(viewModelRequest, second.content.left().request)
        assertFalse(second.loading)
    }

    private fun `build MeUserViewModel`(
        arena: GetUserArena = mockk(relaxed = true),
        initial: Option2<GetUserViewModelRequest> = Option2.from(GetUserViewModelRequest(UserLogin("Makentoshe"))),
    ): GetUserViewModel {
        val androidUserSessionProvider = object : AndroidUserSessionProvider {
            override fun get() = AndroidUserSession(ClientId(""), ClientApi(""), null, null, null)
        }

        return GetUserViewModel(mockk(), androidUserSessionProvider, arena, initial)
    }

    private fun `build MeUserViewModel with default responses`(
        initial: Option2<GetUserViewModelRequest> = Option2.from(GetUserViewModelRequest(UserLogin("Makentoshe"))),
    ): GetUserViewModel {
        val arenaResponse1 = FlowArenaResponse(true, Either2.Left(GetUserArenaResponse(mockk(), mockk())))
        val arenaResponse2 = FlowArenaResponse(false, Either2.Left(GetUserArenaResponse(mockk(), mockk())))
        return `build MeUserViewModel`(`build MeUserArena`(arenaResponse1, arenaResponse2), initial)
    }

    @Suppress("NAME_SHADOWING")
    private fun `build MeUserArena`(vararg responses: FlowArenaResponse<GetUserArenaResponse, Nothing>): GetUserArena {
        val responses = responses.map { FlowArenaResponse(it.loading, it.content.mapRight { ArenaException() }) }

        val mockMeUserArena = mockk<GetUserArena>()
        coEvery { mockMeUserArena.suspendFlowFetch(any()) } returns flowOf(*responses.toTypedArray())
        return mockMeUserArena
    }
}
