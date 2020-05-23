package com.makentoshe.habrachan.view.acticle

import android.content.Intent
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.di.article.ArticleFragmentScope
import com.makentoshe.habrachan.di.article.WebArticleFragmentScope
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.article.web.JavaScriptInterface
import com.makentoshe.habrachan.navigation.article.ArticleFragmentNavigation
import com.makentoshe.habrachan.navigation.article.WebArticleScreen
import com.makentoshe.habrachan.viewmodel.article.ArticleFragmentViewModel
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.article.VoteArticleViewModel
import io.mockk.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

@RunWith(RobolectricTestRunner::class)
class WebArticleFragmentTest : ArticleFragmentTest() {

    private val activityController = Robolectric.buildActivity(AppActivity::class.java, Intent())

    override fun launchScreen(articleId: Int): AppActivity {
        val activity = activityController.setup().get()
        router.navigateTo(WebArticleScreen(articleId))
        return activity
    }

    private val mockJavaScriptInterface = mockk<JavaScriptInterface>(relaxed = true)

    @Before
    fun before() {
        Toothpick.openScopes(
            ApplicationScope::class.java, ArticleFragmentScope::class.java, WebArticleFragmentScope::class.java
        ).installTestModules(module {
            bind<CompositeDisposable>().toInstance(disposables)
            bind<UserAvatarViewModel>().toInstance(mockUserAvatarViewModel)
            bind<ArticleFragmentViewModel>().toInstance(mockArticleViewModel)
            bind<ArticleFragmentNavigation>().toInstance(mockNavigator)
            bind<JavaScriptInterface>().toInstance(mockJavaScriptInterface)
            bind<VoteArticleViewModel>().toInstance(mockVoteArticleViewModel)
        }).inject(this)
    }

    @After
    fun after() {
        Toothpick.closeScope(WebArticleFragmentScope::class.java)
    }

    @Test
    fun testShouldNavigateToResourcesScreen() {
        val articleResourceString = "UrlString"
        every { mockNavigator.toArticleResourceScreen(articleResourceString) } just runs

        val imageObservable = BehaviorSubject.create<String>()
        every { mockJavaScriptInterface.imageObservable } returns imageObservable

        launchScreen()

        imageObservable.onNext(articleResourceString)

        verify { mockNavigator.toArticleResourceScreen(articleResourceString) }
    }

}
