import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.android.common.user.me.arena.MeUserArenaStorage
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaRequest
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaResponse
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserFromArena
import com.makentoshe.habrachan.functional.left
import com.makentoshe.habrachan.functional.rightOrNull
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MeUserArenaStorageTest {

    @Test
    fun `test should check arena storage properly write me in shared preferences`() {
        val sharedPreferences = `build SharedPreferences`()
        val arenaStorage = MeUserArenaStorage(sharedPreferences)

        val customMeUserFromArena = `custom arena user`()
        val arenaRequest = MeUserArenaRequest(AdditionalRequestParameters())
        val arenaResponse = MeUserArenaResponse(arenaRequest, customMeUserFromArena)

        arenaStorage.carry(arenaRequest, arenaResponse)

        val json = sharedPreferences.getString(MeUserArenaStorage.ME_JSON, null)
        assertEquals("{\"alias\":\"Makentoshe\"}", json)
    }

    @Test
    fun `test should check arena storage properly read me from shared preferences`() {
        val sharedPreferences = `build SharedPreferences`().apply {
            edit().putString(MeUserArenaStorage.ME_JSON, "{\"alias\":\"Makentoshe\"}").commit()
        }
        val arenaStorage = MeUserArenaStorage(sharedPreferences)

        val arenaRequest = MeUserArenaRequest(AdditionalRequestParameters())
        val arenaResponse = arenaStorage.fetch(arenaRequest).left()

        assertEquals(arenaRequest, arenaResponse.request)
        assertEquals("Makentoshe", arenaResponse.me.parameters["alias"]?.jsonPrimitive?.contentOrNull)
    }

    @Test
    fun `test should check arena storage can't read me from shared preferences`() {
        val arenaStorage = MeUserArenaStorage(`build SharedPreferences`())

        val arenaRequest = MeUserArenaRequest(AdditionalRequestParameters())
        val arenaResponse = arenaStorage.fetch(arenaRequest).rightOrNull()

        assertTrue(arenaResponse is EmptyArenaStorageException)
    }

    private fun `build SharedPreferences`(): SharedPreferences {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return context.getSharedPreferences("test", Context.MODE_PRIVATE)
    }

    private fun `custom arena user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = MeUserFromArena(parameters)

}
