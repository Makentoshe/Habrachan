@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.entity.android.*
import com.makentoshe.habrachan.entity.article.*
import io.mockk.mockk
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticleTest {

    private val json: String
        get() = javaClass.classLoader.getResourceAsStream("article.json").readAllBytes().decodeToString()

    private val properties: Map<String, JsonElement>
        get() = Json.decodeFromString<JsonObject>(json).toMap()

    private val article: Article
        get() = Article(properties, ArticlePropertiesDelegateImpl(properties) { mockk() })

    @Test
    fun `test should check articleId property`() {
        assertEquals(582308, article.articleId.value.articleId)
    }

    @Test
    fun `test should check articleTitle property`() {
        assertEquals("Слежка и трекеры в Web 3.0", article.articleTitle.value.articleTitle)
    }

    @Test
    fun `test should check isCorporative property`() {
        assertEquals(1, article.isCorporative.value)
    }

    @Test
    fun `test should check textHtml property`() {
        assertEquals("test_text_html", article.textHtml.value)
    }

    @Test
    fun `test should check lang property`() {
        assertEquals("ru", article.lang.value)
    }

    @Test
    fun `test should check editorVersion property`() {
        assertEquals(2, article.editorVersion.value)
    }

    @Test
    fun `test should check postType property`() {
        assertEquals("translation", article.postType.value)
    }

    @Test
    fun `test should check timePublished property`() {
        assertEquals("Fri Oct 08 13:09:50 MSK 2021", article.timePublished.value.timePublishedDate.toString())
        assertEquals("2021-10-08T13:09:50+03:00", article.timePublished.value.timePublishedString)
    }

    @Test
    fun `test should check author property`() {
        assertEquals(2578349, article.author.value.parameters["id"]?.jsonPrimitive?.int)
    }

    @Test
    fun `test should check commentsCount property`() {
        assertEquals(16, article.commentsCount.value)
    }

    @Test
    fun `test should check favoritesCount property`() {
        assertEquals(0, article.favoritesCount.value)
    }

    @Test
    fun `test should check readingCount property`() {
        assertEquals(275, article.readingCount.value)
    }

    @Test
    fun `test should check voteCount property`() {
        assertEquals(15, article.votesCount.value)
    }

    @Test
    fun `test should check scoreCount property`() {
        assertEquals(139, article.scoreCount.value)
    }

    @Test
    fun `test should check flows property`() {
        assertEquals(2, article.flows.value.size)
    }

    @Test
    fun `test should check hubs property`() {
        assertEquals(3, article.hubs.value.size)
    }

    @Test
    fun `test should check tags property`() {
        assertEquals(5, article.tags.value.size)
    }

}
