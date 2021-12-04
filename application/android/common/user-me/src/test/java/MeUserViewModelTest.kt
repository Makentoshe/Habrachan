import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModel
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModelRequest
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSession
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.ClientApi
import com.makentoshe.habrachan.application.android.common.usersession.ClientId
import com.makentoshe.habrachan.application.common.arena.FlowArenaResponse
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArena
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaResponse
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MeUserViewModelTest {

    @get:Rule
    internal var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `test should return success model with initial option 1`() = runBlocking {
        val arenaResponse1 = FlowArenaResponse(true, Either2.Left(MeUserArenaResponse(mockk(), mockk())))
        val arenaResponse2 = FlowArenaResponse(false, Either2.Left(MeUserArenaResponse(mockk(), mockk())))
        val viewModel = `build MeUserViewModel`(`build MeUserArena`(arenaResponse1, arenaResponse2))

        assertEquals(arenaResponse1, viewModel.model.first())
    }

    @Test
    fun `test should return success model with initial option 2`() = runBlocking {
        val arenaResponse1 = FlowArenaResponse(true, Either2.Left(MeUserArenaResponse(mockk(), mockk())))
        val arenaResponse2 = FlowArenaResponse(false, Either2.Left(MeUserArenaResponse(mockk(), mockk())))
        val viewModel = `build MeUserViewModel`(`build MeUserArena`(arenaResponse1, arenaResponse2))

        assertEquals(arenaResponse2, viewModel.model.drop(1).first())
    }

    @Test
    fun `test should return success model with channel 1`() = runBlocking {
        val arenaResponse1 = FlowArenaResponse(true, Either2.Left(MeUserArenaResponse(mockk(), mockk())))
        val arenaResponse2 = FlowArenaResponse(false, Either2.Left(MeUserArenaResponse(mockk(), mockk())))
        val viewModel = `build MeUserViewModel`(`build MeUserArena`(arenaResponse1, arenaResponse2), Option2.None)
        launch { viewModel.channel.send(MeUserViewModelRequest()) }

        assertEquals(arenaResponse1, viewModel.model.first())
    }

    @Test
    fun `test should return success model with channel 2`() = runBlocking {
        val arenaResponse1 = FlowArenaResponse(true, Either2.Left(MeUserArenaResponse(mockk(), mockk())))
        val arenaResponse2 = FlowArenaResponse(false, Either2.Left(MeUserArenaResponse(mockk(), mockk())))
        val viewModel = `build MeUserViewModel`(`build MeUserArena`(arenaResponse1, arenaResponse2), Option2.None)
        launch { viewModel.channel.send(MeUserViewModelRequest()) }

        assertEquals(arenaResponse2, viewModel.model.drop(1).first())
    }

    private fun `build MeUserViewModel`(
        arena: MeUserArena = mockk(relaxed = true),
        initial: Option2<MeUserViewModelRequest> = Option2.from(MeUserViewModelRequest()),
    ): MeUserViewModel {
        val androidUserSessionProvider = object : AndroidUserSessionProvider {
            override fun get() = AndroidUserSession(ClientId(""), ClientApi(""), null, null, null)
        }

        return MeUserViewModel(androidUserSessionProvider, arena, initial)
    }

    private fun `build MeUserArena`(vararg responses: FlowArenaResponse<MeUserArenaResponse>): MeUserArena {
        val mockMeUserArena = mockk<MeUserArena>()
        coEvery { mockMeUserArena.suspendFlowFetch(any()) } returns flowOf(*responses)
        return mockMeUserArena
    }
}
