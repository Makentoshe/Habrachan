import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.view.ContextThemeWrapper
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import com.makentoshe.habrachan.application.android.common.articles.model.ArticlesModelElement
import com.makentoshe.habrachan.entity.Article
import com.maketoshe.habrachan.application.android.screen.articles.page.R
import com.maketoshe.habrachan.application.android.screen.articles.page.model.ArticlesPageAdapter
import com.maketoshe.habrachan.application.android.screen.articles.page.view.ArticlesPageItemViewHolder
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ArticlesPageAdapterTest {

    private val adapter = ArticlesPageAdapter()

    @Test
    fun testShouldInitializeArticle() = runBlocking {
        val mockArticle = mockk<Article>(relaxed = true)
        every { mockArticle.timePublishedRaw } returns "2021-08-12T16:00:01+03:00"

        val mockViewHolder = buildViewHolder()

        adapter.submitData(PagingData.from(listOf(ArticlesModelElement(mockArticle))))
        adapter.onBindViewHolder(mockViewHolder, 0)

        assert(mockViewHolder.time.text.isNotBlank())
    }

    private fun buildViewHolder(): ArticlesPageItemViewHolder {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        val themedContext = ContextThemeWrapper(applicationContext, R.style.Theme_MaterialComponents)
        val inflater = LayoutInflater.from(themedContext)
        val view = inflater.inflate(R.layout.layout_articles_page_item, null, false)
        return spyk(ArticlesPageItemViewHolder(view))
    }
}