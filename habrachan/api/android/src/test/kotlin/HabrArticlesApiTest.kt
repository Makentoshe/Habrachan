import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.android.AndroidHabrApi
import com.makentoshe.habrachan.api.android.articles
import com.makentoshe.habrachan.api.android.articles.*
import com.makentoshe.habrachan.api.articles.ArticlesVote
import com.makentoshe.habrachan.api.articles.DailyArticlesPeriod
import com.makentoshe.habrachan.api.articles.DownVoteReason
import com.makentoshe.habrachan.entity.article.component.ArticleId
import org.junit.Assert.assertEquals
import org.junit.Test

class HabrArticlesApiTest {

    private val api = AndroidHabrApi

    @Test
    fun `test should built filtered articles request with topArticlesFilter`() {
        val parameters = AdditionalRequestParameters(queries = mapOf("include" to "text_html"), headers = mapOf("token" to "asd"))
        val request = api.articles().filter(topArticlesFilter(DailyArticlesPeriod)).build(parameters)

        assertEquals("https://habr.com/api/v1/top/daily", request.path)
        assertEquals("text_html", request.queries["include"])
        assertEquals("asd", request.headers["token"])
    }

    @Test
    fun `test should built filtered articles request with allArticlesFilter`() {
        val parameters = AdditionalRequestParameters(queries = mapOf("include" to "text_html"), headers = mapOf("client" to "3939"))
        val request = api.articles().filter(allArticlesFilter()).build(parameters)

        assertEquals("https://habr.com/api/v1/posts/all", request.path)
        assertEquals("text_html", request.queries["include"])
        assertEquals("3939", request.headers["client"])
    }

    @Test
    fun `test should built filtered articles request with interestingArticlesFilter`() {
        val parameters = AdditionalRequestParameters(queries = mapOf("include" to "text_html"), headers = mapOf("client" to "3939"))
        val request = api.articles().filter(interestingArticlesFilter()).build(parameters)

        assertEquals("https://habr.com/api/v1/posts/interesting", request.path)
        assertEquals("text_html", request.queries["include"])
        assertEquals("3939", request.headers["client"])
    }

    @Test
    fun `test should check built article request`() {
        val parameters = AdditionalRequestParameters(queries = mapOf("page" to "1"))
        val request = api.articles().article(ArticleId(123)).build(parameters)

        assertEquals("https://habr.com/api/v1/post/123", api.articles().article(ArticleId(123)).path)
        assertEquals("1", request.queries["page"])
    }

    @Test
    fun `test should build vote down article request`() {
        val downVote = ArticlesVote.Down(DownVoteReason.DisagreeWithTheArticle)
        val request = api.articles().article(ArticleId(39)).vote(downVote).build(AdditionalRequestParameters())

        assertEquals("https://habr.com/api/v1/post/39/vote", request.path)
        assertEquals("-1", request.queries["vote"])
        assertEquals("22", request.queries["reason"])
    }

    @Test
    fun `test should build vote up article request`() {
        val request = api.articles().article(ArticleId(39)).vote(ArticlesVote.Up).build(AdditionalRequestParameters())

        assertEquals("https://habr.com/api/v1/post/39/vote", request.path)
        assertEquals("1", request.queries["vote"])
    }
}