@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.mobile.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ArticleTest {

    private val json: String
        get() = javaClass.classLoader.getResourceAsStream("article.json").readAllBytes().decodeToString()

    private val properties: Map<String, JsonElement>
        get() = Json.decodeFromString<JsonObject>(json).toMap()

    private val article: Article
        get() = Article(properties, ArticlePropertiesDelegateImpl(properties))

    @Test
    fun `test should check articleId property`() {
        assertEquals(581278, article.articleId.value.articleId)
    }

    @Test
    fun `test should check articleTitle property`() {
        assertEquals("Бум винила, вдогонку за прошлым", article.articleTitle.value.articleTitle)
    }

    @Test
    fun `test should check isCorporative property`() {
        assertFalse(article.isCorporative.value)
    }

    @Test
    fun `test should check textHtml property`() {
        assertEquals("testTextHtml", article.textHtml.getOrNull())
    }

    @Test
    fun `test should check lang property`() {
        assertEquals("ru", article.lang.value)
    }

    @Test
    fun `test should check editorVersion property`() {
        assertEquals("2.0", article.editorVersion.value)
    }

    @Test
    fun `test should check postType property`() {
        assertEquals("article", article.postType.value)
    }

    @Test
    fun `test should check commentsEnabled property`() {
        assert(article.commentsEnabled.value)
    }

    @Test
    fun `test should check votesEnabled property`() {
        assert(article.votesEnabled.value)
    }

    @Test
    fun `test should check isEditorial property`() {
        assertFalse(article.isEditorial.value)
    }

    @Test
    fun `test should check timePublished property`() {
        assertEquals("Sun Oct 03 15:25:43 MSK 2021", article.timePublished.value.timePublishedDate.toString())
        assertEquals("2021-10-03T15:25:43+00:00", article.timePublished.value.timePublishedString)
    }

    @Test
    fun `test should check author property`() {
        assertEquals(365505, article.author.value.parameters["id"]?.jsonPrimitive?.int)
    }

    @Test
    fun `test should check commentsCount property`() {
        assertEquals(4, article.commentsCount.value)
    }

    @Test
    fun `test should check favoritesCount property`() {
        assertEquals(0, article.favoritesCount.value)
    }

    @Test
    fun `test should check readingCount property`() {
        assertEquals(705, article.readingCount.value)
    }

    @Test
    fun `test should check voteCount property`() {
        assertEquals(2, article.votesCount.value)
    }

    @Test
    fun `test should check scoreCount property`() {
        assertEquals(2, article.scoreCount.value)
    }

    @Test
    fun `test should check flows property`() {
        assertEquals(1, article.flows.value.size)
    }

    @Test
    fun `test should check hubs property`() {
        assertEquals(1, article.hubs.value.size)
    }

    @Test
    fun `test should check tags property`() {
        assertEquals(4, article.tags.value.size)
    }
}
