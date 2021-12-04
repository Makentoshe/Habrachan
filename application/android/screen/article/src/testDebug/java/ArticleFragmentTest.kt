import android.content.res.Resources
import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.di.ArticleScope
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.ArticleCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.ContentScreenNavigator
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.Option
import io.mockk.mockk
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.coroutines.CoroutineScope
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.smoothie.lifecycle.closeOnDestroy

@RunWith(AndroidJUnit4::class)
class ArticleFragmentTest {

    private val articleId = articleId(39)

    private val mockResources = mockk<Resources>()
    private val mockCoroutineScope = mockk<CoroutineScope>()
    private val mockGetArticleViewModel = mockk<GetArticleViewModel>(relaxed = true)
    private val mockGetAvatarViewModel = mockk<GetAvatarViewModel>(relaxed = true)
    private val mockVoteArticleViewModel = mockk<VoteArticleViewModel>(relaxed = true)
    private val mockContentScreenNavigator = mockk<ContentScreenNavigator>(relaxed = true)
    private val mockArticleCommentsScreenNavigator = mockk<ArticleCommentsScreenNavigator>(relaxed = true)

    private val toothpickModule = module {
        bind<ArticleId>().toInstance(articleId)
        bind<Resources>().toInstance(mockResources)
        bind<CoroutineScope>().toInstance(mockCoroutineScope)
        bind<GetAvatarViewModel>().toInstance(mockGetAvatarViewModel)
        bind<VoteArticleViewModel>().toInstance(mockVoteArticleViewModel)
        bind<GetArticleViewModel>().toInstance(mockGetArticleViewModel)
        bind<ContentScreenNavigator>().toInstance(mockContentScreenNavigator)
        bind<ArticleCommentsScreenNavigator>().toInstance(mockArticleCommentsScreenNavigator)
    }

    private val toothpickScope = Toothpick.openScopes(
        ApplicationScope::class, ArticleScope::class
    ).openSubScope(ArticleScope(articleId)).installModules(toothpickModule)

    @Test
    @Ignore("No factory could be found for class com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.BackwardNavigator. Check that the class has either a @Inject annotated constructor or contains @Inject annotated members.")
    fun `test should check progress bar visibility on initial state`() {
        `launch and resume fragment than execute` {
            fragment_article_progress `visibility should be` View.VISIBLE
        }.`and release at finish`()
    }

    private fun `launch and resume fragment than execute`(
        action: ArticleFragment.() -> Unit
    ): FragmentScenario<ArticleFragment> {
        return ArticleFragment.build(articleId, Option.None).`launch this fragment and execute` {
            toothpickScope.closeOnDestroy(this).inject(this)
        }.`then resume and execute`(action)
    }
}
