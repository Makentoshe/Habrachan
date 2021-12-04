import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.paging.LoadState
import androidx.test.core.app.ApplicationProvider
import com.makentoshe.habrachan.application.android.screen.articles.R
import com.makentoshe.habrachan.application.android.screen.articles.databinding.LayoutArticlesFooterBinding
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesPageAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesPageFooterAdapter
import com.makentoshe.habrachan.application.android.screen.articles.view.ArticlesFooterViewHolder
import com.makentoshe.habrachan.application.android.screen.articles.view.finish
import com.makentoshe.habrachan.application.android.screen.articles.view.loading
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.network.exception.GetArticlesException
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.UnknownHostException

@RunWith(RobolectricTestRunner::class)
class ArticlesPageFooterAdapterTest {

    private val mockArticlesPageAdapter = mockk<ArticlesPageAdapter>()
    private val adapter = ArticlesPageFooterAdapter(mockArticlesPageAdapter)

    @Test
    fun testShouldBindViewHolderOnPaginationEnd() {
        val mockViewHolder = mockk<ArticlesFooterViewHolder>(relaxed = true)

        val mockLoadState = mockk<LoadState>()
        every { mockLoadState.endOfPaginationReached } returns true

        adapter.onBindViewHolder(mockViewHolder, mockLoadState)

        verify { mockViewHolder.finish() }
    }

    @Test
    fun testShouldBindViewHolderOnLoadingProgress() {
        val mockViewHolder = mockk<ArticlesFooterViewHolder>(relaxed = true)

        val mockLoadState = mockk<LoadState.Loading>()
        every { mockLoadState.endOfPaginationReached } returns false

        adapter.onBindViewHolder(mockViewHolder, mockLoadState)

        verify { mockViewHolder.loading() }
    }

    @Test
    fun testShouldBindViewHolderOnUndefinedError() {
        val mockViewHolder = viewHolder()

        val mockLoadState = mockk<LoadState.Error>()
        every { mockLoadState.endOfPaginationReached } returns false
        every { mockLoadState.error } returns Exception("Message")

        val title = mockViewHolder.context.getString(R.string.articles_footer_exception_title)
        adapter.onBindViewHolder(mockViewHolder, mockLoadState)

        assertEquals(View.INVISIBLE, mockViewHolder.progress.visibility)
        assertEquals(View.VISIBLE, mockViewHolder.retry.visibility)
        assertEquals(View.VISIBLE, mockViewHolder.message.visibility)
        assertEquals(mockLoadState.error.toString(), mockViewHolder.message.text)
        assertEquals(View.VISIBLE, mockViewHolder.title.visibility)
        assertEquals(title, mockViewHolder.title.text)
    }

    @Test
    fun testShouldBindViewHolderViaExceptionHandler() {
        val mockViewHolder = viewHolder()

//        TODO(fix handling arena exceptions)
//        val title = mockViewHolder.context.getString(R.string.exception_handler_network_unknown_host_title)
//        val message = mockViewHolder.context.getString(R.string.exception_handler_network_unknown_host_message)
//        every { mockExceptionHandler.handle(any<Throwable>()) } returns ExceptionEntry(title, message, Throwable())

        val title = mockViewHolder.context.getString(R.string.exception_handler_unknown_title)
        val message = mockViewHolder.context.getString(R.string.exception_handler_unknown_message)

        val mockLoadState = mockk<LoadState.Error>()
        every { mockLoadState.endOfPaginationReached } returns false
        every { mockLoadState.error } returns mockk<ArenaException>().also { arenaException ->
            every { arenaException.sourceException } returns mockk<GetArticlesException>().also { mockGetArticlesException ->
                every { mockGetArticlesException.cause } returns UnknownHostException("Message")
            }
        }

        adapter.onBindViewHolder(mockViewHolder, mockLoadState)

        assertEquals(View.INVISIBLE, mockViewHolder.progress.visibility)
        assertEquals(View.VISIBLE, mockViewHolder.retry.visibility)
        assertEquals(View.VISIBLE, mockViewHolder.message.visibility)
        assertEquals(View.VISIBLE, mockViewHolder.title.visibility)
        assertEquals(title, mockViewHolder.title.text)
        assertEquals(message, mockViewHolder.message.text)
    }

    private fun viewHolder(): ArticlesFooterViewHolder {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        val themedContext = ContextThemeWrapper(applicationContext, R.style.Theme_MaterialComponents)
        return spyk(ArticlesFooterViewHolder(LayoutArticlesFooterBinding.inflate(LayoutInflater.from(themedContext))))
    }

}