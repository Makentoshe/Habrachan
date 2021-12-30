@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.entity.mobile.ArticleAuthorPropertiesDelegateImpl
import com.makentoshe.habrachan.entity.mobile.ArticleHubPropertiesDelegateImpl
import com.makentoshe.habrachan.entity.mobile.ArticlePropertiesDelegateImpl
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticlePropertiesDelegateImplTest {

    @Test
    fun `test should check article id property for success json`() {
        val json = javaClass.classLoader.getResourceAsStream("article_success.json").readBytes().decodeToString()
        delegate(json).apply {
            assertEquals(598425, articleId.value.articleId)
        }
    }

    @Test
    fun `test should check author login for success json`() {
        val json = javaClass.classLoader.getResourceAsStream("article_success.json").readBytes().decodeToString()
        delegate(json).apply {
            assertEquals("SearchInform_team", author.value.delegate.authorLogin.value.authorLogin)
        }
    }

    @Test
    fun `test should check first hub id for success json`() {
        val json = javaClass.classLoader.getResourceAsStream("article_success.json").readBytes().decodeToString()
        delegate(json).apply {
            assertEquals(22418, hubs.value.first().delegate.hubId.value.hubId)
        }
    }

    private fun delegate(json: String) = ArticlePropertiesDelegateImpl(
        Json.decodeFromString<JsonObject>(json).toMap(),
        { ArticleAuthorPropertiesDelegateImpl(it) },
        { ArticleHubPropertiesDelegateImpl(it) }
    )
}