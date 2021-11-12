import androidx.test.ext.junit.runners.AndroidJUnit4
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesFlowAdapter
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticlesFlowAdapterTest {

    private val articlesFlowAdapter = ArticlesFlowAdapter(mockk(relaxed = true), 39)

    @Test
    fun testShouldCheckAdapterSize() {
        assertEquals(39, articlesFlowAdapter.itemCount)
    }

    @Test
    fun testShouldCheckAdapterFactory1() {
        val f = articlesFlowAdapter.createFragment(5)
        assertEquals(5, f.arguments.index)
    }
}