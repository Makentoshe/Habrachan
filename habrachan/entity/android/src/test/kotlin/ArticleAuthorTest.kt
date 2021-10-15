@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.entity.android.*
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.author
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.author.authorAvatar
import com.makentoshe.habrachan.entity.article.author.authorId
import com.makentoshe.habrachan.entity.article.author.authorLogin
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticleAuthorTest {

    private val json: String
        get() = javaClass.classLoader.getResourceAsStream("article.json").readAllBytes().decodeToString()

    private val properties: Map<String, JsonElement>
        get() = Json.decodeFromString<JsonObject>(json).toMap()

    private val author: ArticleAuthor
        get() = Article(properties, ArticlePropertiesDelegateImpl(properties) { parameters ->
            ArticleAuthorPropertiesDelegateImpl(parameters)
        }).author.value

    @Test
    fun `test should check authorId property`() {
        assertEquals(2578349, author.authorId.value.authorId)
    }

    @Test
    fun `test should check login property`() {
        assertEquals("BraveSoftware", author.authorLogin.value.authorLogin)
    }

    @Test
    fun `test should check fullname property`() {
        assert(author.fullname.isEmpty)
    }

    @Test
    fun `test should check speciality property`() {
        assertEquals("sas", author.speciality.getOrThrow())
    }

    @Test
    fun `test should check rating property`() {
        assertEquals(3.6f, author.rating.value)
    }

    @Test
    fun `test should check avatar property`() {
        assertEquals("https://habr.com/images/avatars/stub-user-middle.gif", author.authorAvatar.getOrNull()?.avatarUrl)
    }

}
