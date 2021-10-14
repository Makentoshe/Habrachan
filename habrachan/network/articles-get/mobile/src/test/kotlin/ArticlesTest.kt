import com.makentoshe.habrachan.network.articles.get.entity.Articles
import com.makentoshe.habrachan.network.articles.get.entity.articles
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticlesTest {

    private val json = javaClass.classLoader.getResourceAsStream("articles_success.json").readAllBytes().decodeToString()

    private val articles = Articles(Json.decodeFromString<Map<String, JsonElement>>(json).toMap())

    @Test
    fun `test should check articles property`() {
        val articles = articles.articles.value
        assertEquals(20, articles.size)
    }

}