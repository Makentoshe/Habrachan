import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.ArticlesVote
import com.makentoshe.habrachan.api.articles.DailyArticlesPeriod
import com.makentoshe.habrachan.api.articles.DownVoteReason
import com.makentoshe.habrachan.api.articles.and
import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.articles
import com.makentoshe.habrachan.api.mobile.articles.*
import com.makentoshe.habrachan.entity.article.component.ArticleId
import org.junit.Assert.assertEquals
import org.junit.Test

class HabrArticlesApiTest {

    private val api = MobileHabrApi

    @Test
    fun `test should built filtered articles request`() {
        val parameters = AdditionalRequestParameters(headers = mapOf("token" to "asd"))
        val filters = sortArticlesFilter(DateArticlesSort) and periodArticlesFilter(DailyArticlesPeriod)
        val request = api.articles().filter(*filters).build(parameters)

        assertEquals("https://habr.com/kek/v2/articles/", request.path)
        assertEquals(DateArticlesSort.value, request.queries["sort"])
        assertEquals(DailyArticlesPeriod.value, request.queries["period"])
        assertEquals("asd", request.headers["token"])
    }

    @Test
    fun `test should check built article request`() {
        assertEquals("https://habr.com/kek/v2/articles/123", api.articles().article(ArticleId(123)).path)
    }

    @Test
    fun `test should build most reading articles request`() {
        val parameters = AdditionalRequestParameters(queries = mapOf("page" to "1"))
        val request = api.articles().filter(mostReadingArticlesFilter()).build(parameters)

        assertEquals("https://habr.com/kek/v2/articles/most-reading", request.path)
    }

    @Test
    fun `test should build vote down article request`() {
        val downVote = ArticlesVote.Down(DownVoteReason.DisagreeWithTheArticle)
        val request = api.articles().article(ArticleId(39)).vote(downVote).build(AdditionalRequestParameters())

        assertEquals("https://habr.com/kek/v2/articles/39/vote", request.path)
        assertEquals("-1", request.queries["vote"])
        assertEquals("{\"reason\":\"22\"}", request.body)
    }

    @Test
    fun `test should build vote up article request`() {
        val request = api.articles().article(ArticleId(39)).vote(ArticlesVote.Up).build(AdditionalRequestParameters())

        assertEquals("https://habr.com/kek/v2/articles/39/vote", request.path)
        assertEquals("1", request.queries["vote"])
    }
}

