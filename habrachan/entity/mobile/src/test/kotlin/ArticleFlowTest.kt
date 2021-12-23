@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.flow.ArticleFlow
import com.makentoshe.habrachan.entity.mobile.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticleFlowTest {

    private val json: String
        get() = javaClass.classLoader.getResourceAsStream("article.json").readBytes().decodeToString()

    private val properties: Map<String, JsonElement>
        get() = Json.decodeFromString<JsonObject>(json).toMap()

    private val articlePropertiesDelegate
        get() = ArticlePropertiesDelegateImpl(
            parameters = properties,
            articleAuthorPropertiesDelegateFactory = { ArticleAuthorPropertiesDelegateImpl(it) },
            articleHubPropertiesDelegateFactory = { ArticleHubPropertiesDelegateImpl(it) })

    private val flow: ArticleFlow
        get() = Article(properties, articlePropertiesDelegate).flows.value.first()

    @Test
    fun `test should check id property`() {
        assertEquals(7, flow.flowId.value.flowId)
    }

    @Test
    fun `test should check title property`() {
        assertEquals("PopSci", flow.title.value)
    }
}
