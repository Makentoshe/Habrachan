@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.author
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.author.authorAvatar
import com.makentoshe.habrachan.entity.article.author.authorId
import com.makentoshe.habrachan.entity.article.author.authorLogin
import com.makentoshe.habrachan.entity.mobile.*
import com.makentoshe.habrachan.functional.Option2
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

    private val articlePropertiesDelegate
        get() = ArticlePropertiesDelegateImpl(
            parameters = properties,
            articleAuthorPropertiesDelegateFactory = { ArticleAuthorPropertiesDelegateImpl(it) },
            articleHubPropertiesDelegateFactory = { ArticleHubPropertiesDelegateImpl(it) })

    private val author: ArticleAuthor
        get() = Article(properties, articlePropertiesDelegate).author.value

    @Test
    fun `test should check authorId property`() {
        assertEquals(365505, author.authorId.value.authorId)
    }

    @Test
    fun `test should check login property`() {
        assertEquals("Gromushka", author.authorLogin.value.authorLogin)
    }

    @Test
    fun `test should check fullname property`() {
        assertEquals("Александр", author.fullname.value)
    }

    @Test
    fun `test should check speciality property`() {
        assertEquals("Программист", author.speciality.value)
    }

    @Test
    fun `test should check rating property`() {
        assertEquals(12.3f, author.rating.value)
    }

    @Test
    fun `test should check avatar property`() {
        assertEquals(Option2.None, author.authorAvatar)
    }

}
