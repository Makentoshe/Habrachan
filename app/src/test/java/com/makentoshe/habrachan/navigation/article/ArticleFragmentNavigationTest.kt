package com.makentoshe.habrachan.navigation.article

import com.makentoshe.habrachan.BaseRobolectricTest
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.comments.CommentsScreen
import com.makentoshe.habrachan.navigation.images.OverlayImageScreen
import com.makentoshe.habrachan.navigation.user.UserScreen
import com.makentoshe.habrachan.navigation.comments.CommentsScreenArguments
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.terrakok.cicerone.Router

@RunWith(RobolectricTestRunner::class)
class ArticleFragmentNavigationTest : BaseRobolectricTest() {

    private lateinit var articleFragmentNavigation: ArticleFragmentNavigation

    private val mockRouter = mockk<Router>(relaxed = true)
    private val mockSessionDao = mockk<SessionDao>()

    @Before
    fun before() {
        articleFragmentNavigation = ArticleFragmentNavigation(mockRouter, mockSessionDao)
    }

    @Test
    fun testShouldNavigateToCommentsScreenById() {
        val articleId = 1

        articleFragmentNavigation.toArticleCommentsScreen(articleId)

        val screen = slot<CommentsScreen>()
        verify { mockRouter.navigateTo(capture(screen)) }
        val commentsScreenArguments =
            CommentsScreenArguments(screen.captured.fragment)
        assertEquals(articleId, commentsScreenArguments.articleId)
    }

    @Test
    fun testShouldNavigateToCommentsScreenByArticle() {
        val article = mockk<Article>(relaxed = true)
        every { article.id } returns 1

        mockkObject(Article.Companion)
        every { Article.fromJson(any()) } returns article

        articleFragmentNavigation.toArticleCommentsScreen(article)

        val screen = slot<CommentsScreen>()
        verify { mockRouter.navigateTo(capture(screen)) }
        val commentsScreenArguments =
            CommentsScreenArguments(screen.captured.fragment)
        assertEquals(article, commentsScreenArguments.article)

        unmockkObject(Article.Companion)
    }

    @Test
    fun testShouldNavigateToUserScreenIfLoggedIn() {
        every { mockSessionDao.get().isLoggedIn } returns true
        val userName = "UserName"

        assert(articleFragmentNavigation.toUserScreen(userName))

        val screen = slot<UserScreen>()
        verify { mockRouter.navigateTo(capture(screen)) }
        assertEquals(userName, (screen.captured.fragment.arguments.userAccount as UserAccount.User).userName)
    }

    @Test
    fun testShouldNotNavigateToUserScreenIfNotLoggedIn() {
        every { mockSessionDao.get().isLoggedIn } returns false
        val userName = "UserName"

        assert(!articleFragmentNavigation.toUserScreen(userName))

        verify(exactly = 0) { mockRouter.navigateTo(any()) }
    }

    @Test
    fun testShouldNavigateToResourceScreen() {
        val resource = "resource"

        articleFragmentNavigation.toArticleResourceScreen(resource)
        val screen = slot<OverlayImageScreen>()
        verify { mockRouter.navigateTo(capture(screen)) }
        assertEquals(resource, screen.captured.fragment.arguments.source)
    }

    @Test
    fun testShouldNavigateToBack() {
        articleFragmentNavigation.back()
        verify { mockRouter.exit() }
    }
}
