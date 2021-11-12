import android.content.res.Resources
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.tabs.TabLayout
import com.makentoshe.habrachan.application.android.screen.articles.R
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesUserSearch
import com.makentoshe.habrachan.application.android.screen.articles.model.TabLayoutMediatorController
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TabLayoutMediatorControllerTest {

    private val mockResources = mockk<Resources>()
    private val controller = TabLayoutMediatorController(listOf(
        ArticlesUserSearch("AllArticlesTest", true),
        ArticlesUserSearch("InterestingTest", true),
    ))

    @Test
    fun testCheckStrategy() {
        val tabString = "InterestingTest"

        val mockTab = mockk<TabLayout.Tab>(relaxed = true)
        every { mockResources.getString(R.string.articles_type_interesting) } returns tabString

        controller.strategy(mockTab, 1)
        verify { mockTab.text = tabString }
    }
}