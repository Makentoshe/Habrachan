@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.entity.android.ArticleAuthorPropertiesDelegateImpl
import com.makentoshe.habrachan.entity.android.ArticleHubPropertiesDelegateImpl
import com.makentoshe.habrachan.entity.android.ArticlePropertiesDelegateImpl
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.article.hub.hubId
import com.makentoshe.habrachan.entity.article.hub.title
import com.makentoshe.habrachan.entity.article.hubs
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticleHubTest {

    private val json: String
        get() = javaClass.classLoader.getResourceAsStream("article.json").readAllBytes().decodeToString()

    private val properties: Map<String, JsonElement>
        get() = Json.decodeFromString<JsonObject>(json).toMap()

    private val articlePropertiesDelegate
        get() = ArticlePropertiesDelegateImpl(
            parameters = properties,
            articleAuthorPropertiesDelegateFactory = { ArticleAuthorPropertiesDelegateImpl(it) },
            articleHubPropertiesDelegateFactory = { ArticleHubPropertiesDelegateImpl(it) })

    private val hub: ArticleHub
        get() = Article(properties, articlePropertiesDelegate).hubs.value.first()

    @Test
    fun `test should check id property`() {
        assertEquals(22678, hub.hubId.value.hubId)
    }

    @Test
    fun `test should check title property`() {
        assertEquals("Блог компании Brave", hub.title.value)
    }
}
