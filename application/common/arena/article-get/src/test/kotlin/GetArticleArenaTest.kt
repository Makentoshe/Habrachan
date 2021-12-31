import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.article.get.ArticleFromArena
import com.makentoshe.habrachan.application.common.arena.article.get.GetArticleArena
import com.makentoshe.habrachan.application.common.arena.article.get.GetArticleArenaRequest
import com.makentoshe.habrachan.application.common.arena.article.get.GetArticleArenaResponse
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.component.ArticleId
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.left
import com.makentoshe.habrachan.functional.rightOrNull
import com.makentoshe.habrachan.network.article.get.GetArticleException
import com.makentoshe.habrachan.network.article.get.GetArticleManager
import com.makentoshe.habrachan.network.article.get.GetArticleRequest
import com.makentoshe.habrachan.network.article.get.GetArticleResponse
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

class GetArticleArenaTest {

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
        assertEquals(expectedResponse.article.parameters, flowArenaResponse.content.left().article.parameters)
    }

    @Test
    fun `test should check failure load from source`() = runBlocking {
        val manager = `build user manager`(Either2.Right(GetArticleException(mockk(), null, emptyMap())))

        val meUserArena = `build arena`(`with manager` = manager)
        val flowArenaResponse = meUserArena.suspendFlowFetch(mockk(relaxed = true)).drop(1).first()

        // is failure content and expected exception returned
        assertTrue(flowArenaResponse.content.isRight())
        assertTrue(flowArenaResponse.content.rightOrNull()?.sourceException is GetArticleException)
    }


    private fun `build arena`(
        `with manager`: GetArticleManager = `build user manager`(
            Either2.Right(GetArticleException(mockk(), null, emptyMap()))
        ),
        `with storage`: ArenaCache3<GetArticleArenaRequest, GetArticleArenaResponse> = `build arena storage`(
            Either2.Right(ArenaStorageException("Stub"))
        ),
    ) = GetArticleArena(`with manager`, `with storage`)

    private fun `build arena storage`(
        response: Either2<GetArticleArenaResponse, ArenaStorageException>,
    ): ArenaCache3<GetArticleArenaRequest, GetArticleArenaResponse> {
        val mockArenaStorage = mockk<ArenaCache3<GetArticleArenaRequest, GetArticleArenaResponse>>(relaxed = true)
        every { mockArenaStorage.fetch(any()) } returns response
        return mockArenaStorage
    }

    private fun `build user manager`(
        response: Either2<GetArticleResponse, GetArticleException>,
    ): GetArticleManager {
        val mockMeUserManager = mockk<GetArticleManager>()
        coEvery { mockMeUserManager.execute(any()) } returns response
        return mockMeUserManager
    }

    private fun `custom network user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = Article(parameters, mockk())

    private fun `custom arena user`(
        parameters: Map<String, JsonElement> = mapOf("alias" to JsonPrimitive("Makentoshe")),
    ) = ArticleFromArena(parameters)

    private fun `build manager response`(`with user`: Article): GetArticleResponse {
        val parameters = AdditionalRequestParameters()
        return GetArticleResponse(GetArticleRequest(ArticleId(123), parameters), `with user`)
    }

    private fun `build storage response`(`with user`: ArticleFromArena): GetArticleArenaResponse {
        val parameters = AdditionalRequestParameters()
        return GetArticleArenaResponse(GetArticleArenaRequest(ArticleId(123), parameters), `with user`)
    }
}