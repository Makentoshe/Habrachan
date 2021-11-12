import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.view.ContextThemeWrapper
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import com.makentoshe.habrachan.application.android.common.articles.model.ArticleModel
import com.makentoshe.habrachan.application.android.screen.articles.R
import com.makentoshe.habrachan.application.android.screen.articles.databinding.LayoutArticlesPageItemBinding
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesPageAdapter
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.ArticleScreenNavigator
import com.makentoshe.habrachan.application.android.screen.articles.view.ArticlesPageItemViewHolder
import com.makentoshe.habrachan.entity.article.article
import com.makentoshe.habrachan.entity.article.author.articleAuthor
import com.makentoshe.habrachan.entity.article.hub.articleHub
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ArticlesPageAdapterTest {

    private val mockArticleScreenNavigator = mockk<ArticleScreenNavigator>()
    private val adapter = ArticlesPageAdapter(mockArticleScreenNavigator)

    private val article = article(
        articleId = 1,
        articleTitle = "Title",
        articleAuthor = articleAuthor(1, "login", "avatar"),
        timePublished = "2021-08-12T16:00:01+03:00",
        articleHubs = listOf(articleHub(1, "TestHub1"), articleHub(2, "TestHub2")),
        articleText = "text",
        commentsCount = 1,
        favoritesCount = 2,
        readingCount = 3,
        votesCount = 4,
        scoresCount = 5
    )

    @Test
    fun testShouldInitializeArticle() = runBlocking {
        val mockViewHolder = buildViewHolder()

        adapter.submitData(PagingData.from(listOf(ArticleModel(article))))
        adapter.onBindViewHolder(mockViewHolder, 0)

        assert(mockViewHolder.time.text.isNotBlank())
    }

    @Test
    fun `test should check article title in view holder`() = runBlocking {
        val mockViewHolder = buildViewHolder()

        adapter.submitData(PagingData.from(listOf(ArticleModel(article))))
        adapter.onBindViewHolder(mockViewHolder, 0)

        assertEquals("Title", mockViewHolder.title.text)
    }

    @Test
    fun `test should check article author in view holder`() = runBlocking {
        val mockViewHolder = buildViewHolder()

        adapter.submitData(PagingData.from(listOf(ArticleModel(article))))
        adapter.onBindViewHolder(mockViewHolder, 0)

        assertEquals("login", mockViewHolder.author.text)
    }

    @Test
    fun `test should check article comments count in view holder`() = runBlocking {
        val mockViewHolder = buildViewHolder()

        adapter.submitData(PagingData.from(listOf(ArticleModel(article))))
        adapter.onBindViewHolder(mockViewHolder, 0)

        assertEquals("1", mockViewHolder.commentsCount.text.toString())
    }

    @Test
    fun `test should check article reading count in view holder`() = runBlocking {
        val mockViewHolder = buildViewHolder()

        adapter.submitData(PagingData.from(listOf(ArticleModel(article))))
        adapter.onBindViewHolder(mockViewHolder, 0)

        assertEquals("3", mockViewHolder.readingCount.text.toString())
    }

    @Test
    fun `test should check article scores count in view holder`() = runBlocking {
        val mockViewHolder = buildViewHolder()

        adapter.submitData(PagingData.from(listOf(ArticleModel(article))))
        adapter.onBindViewHolder(mockViewHolder, 0)

        assertEquals("5", mockViewHolder.scoreCount.text.toString())
    }

    @Test
    fun `test should check article hubs in view holder`() = runBlocking {
        val mockViewHolder = buildViewHolder()

        adapter.submitData(PagingData.from(listOf(ArticleModel(article))))
        adapter.onBindViewHolder(mockViewHolder, 0)

        assertEquals("TestHub1, TestHub2", mockViewHolder.hubs.text)
    }

    private fun buildViewHolder(): ArticlesPageItemViewHolder {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        val themedContext = ContextThemeWrapper(applicationContext, R.style.Theme_MaterialComponents)
        return spyk(ArticlesPageItemViewHolder(LayoutArticlesPageItemBinding.inflate(LayoutInflater.from(themedContext))))
    }
}