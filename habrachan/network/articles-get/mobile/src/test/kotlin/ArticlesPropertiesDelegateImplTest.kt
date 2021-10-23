@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.network.articles.get.mobile.entity.ArticlesPropertiesDelegateImpl
import io.mockk.mockk
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ArticlesPropertiesDelegateImplTest {

    @Test
    fun `test should check articles property for default json`() {
        val json = javaClass.classLoader.getResourceAsStream("articles_success.json").readAllBytes().decodeToString()
        val articles = delegate(json)

        assertEquals(20, articles.articles.value.size)
    }

    @Test
    fun `test should check totalPages property for default json`() {
        val json = javaClass.classLoader.getResourceAsStream("articles_success.json").readAllBytes().decodeToString()
        val articles = delegate(json)

        assertEquals(50, articles.totalPages.getOrNull())
    }

    @Test
    fun `test should check articles property for most-reading json`() {
        val json = javaClass.classLoader.getResourceAsStream("articles_mostreading_success.json").readAllBytes().decodeToString()
        val articles = delegate(json)

        assertEquals(20, articles.articles.value.size)
    }

    @Test
    fun `test should check totalPages property for most-reading json`() {
        val json = javaClass.classLoader.getResourceAsStream("articles_mostreading_success.json").readAllBytes().decodeToString()
        val articles = delegate(json)

        assertNull(articles.totalPages.getOrNull())
    }

    private fun delegate(json: String): ArticlesPropertiesDelegateImpl {
        return ArticlesPropertiesDelegateImpl(Json.decodeFromString<JsonObject>(json).toMap()) { mockk() }
    }
}