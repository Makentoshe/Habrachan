import androidx.test.ext.junit.runners.AndroidJUnit4
import com.makentoshe.habrachan.application.android.screen.articles.common.navigation.ArticlesPageFactory
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesFlowAdapter
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.AvailableSpecTypes
import com.makentoshe.habrachan.network.request.SpecType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticlesFlowAdapterTest {

    private val mockArticlesPageFactory = mockk<com.makentoshe.habrachan.application.android.screen.articles.common.navigation.ArticlesPageFactory>(relaxed = true)
    private val mockAvailableSpecTypes = mockk<AvailableSpecTypes>()
    private val articlesFlowAdapter = ArticlesFlowAdapter(
        mockk(relaxed = true), mockArticlesPageFactory, mockAvailableSpecTypes,
    )

    @Before
    fun before() {
        every { mockAvailableSpecTypes.size } returns 39
        every { mockAvailableSpecTypes[any()] } returns mockk()
    }

    @Test
    fun testShouldCheckAdapterSize() {
        assertEquals(39, articlesFlowAdapter.itemCount)
    }

    @Test
    fun testShouldCheckAdapterFactory1() {
        val mockSpecType = mockk<SpecType>()
        every { mockAvailableSpecTypes[5] } returns mockSpecType

        articlesFlowAdapter.createFragment(5)

        verify { mockArticlesPageFactory.build(mockSpecType) }
    }
}