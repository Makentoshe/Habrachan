import android.content.Intent
import android.net.Uri
import com.makentoshe.habrachan.application.android.Launcher
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleScreen2
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesUserSearch
import com.makentoshe.habrachan.application.android.screen.articles.flow.navigation.ArticlesFlowScreen
import com.makentoshe.habrachan.application.android.screen.comments.articles.navigation.ArticleCommentsScreen
import com.makentoshe.habrachan.application.android.screen.comments.thread.navigation.ThreadCommentsScreen
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LauncherTest : TestCase() {

    private val articlesUserSearches = listOf(ArticlesUserSearch("Sas", true))
    private val launcher = Launcher(ArticlesFlowScreen(articlesUserSearches))

    @Test
    fun testShouldOpenDiscussionCommentsScreen1() {
        val intent = intent(Uri.parse("https://habr.com/en/post/571418/#comment_23344290"))
        val screen = launcher.deeplink(intent) as ThreadCommentsScreen

        assertEquals(571418, screen.articleId)
        assertEquals(23344290, screen.commentId)
    }

    @Test
    fun testShouldOpenDiscussionCommentsScreen2() {
        val intent = intent(Uri.parse("https://habr.com/en/post/571418/comments/#comment_23344290"))
        val screen = launcher.deeplink(intent) as ThreadCommentsScreen

        assertEquals(571418, screen.articleId)
        assertEquals(23344290, screen.commentId)
    }

    @Test
    fun testShouldOpenArticleCommentsScreen1() {
        val intent = intent(Uri.parse("https://habr.com/en/post/571418/comments/"))
        val screen = launcher.deeplink(intent) as ArticleCommentsScreen

        assertEquals(571418, screen.articleId.articleId)
    }

    @Test
    fun testShouldOpenArticleCommentsScreen2() {
        val intent = intent(Uri.parse("https://habr.com/ru/company/flipperdevices/blog/564570/comments/"))
        val screen = launcher.deeplink(intent) as ArticleCommentsScreen

        assertEquals(564570, screen.articleId.articleId)
    }

    @Test
    fun testShouldOpenUserScreen1() {
        val intent = intent(Uri.parse("https://habr.com/en/users/makentoshe/"))
        val screen = launcher.deeplink(intent) as UserScreen

        assertEquals("makentoshe", (screen.account as UserAccount.User).login)
    }

    @Test
    fun testShouldOpenUserScreen2() {
        val intent = intent(Uri.parse("https://habr.com/users/makentoshe/"))
        val screen = launcher.deeplink(intent) as UserScreen

        assertEquals("makentoshe", (screen.account as UserAccount.User).login)
    }

    @Test
    fun testShouldOpenCorporateBlogArticleScreen() {
        val intent = intent(Uri.parse("https://habr.com/ru/company/flipperdevices/blog/564570/"))
        val screen = launcher.deeplink(intent) as ArticleScreen2

        assertEquals(564570, screen.articleId.articleId)
    }

    @Test
    fun testShouldOpenArticleScreen1() {
        val intent = intent(Uri.parse("https://habr.com/post/564570/"))
        val screen = launcher.deeplink(intent) as ArticleScreen2

        assertEquals(564570, screen.articleId.articleId)
    }

    @Test
    fun testShouldOpenArticleScreen2() {
        val intent = intent(Uri.parse("https://habr.com/ru/post/564570/"))
        val screen = launcher.deeplink(intent) as ArticleScreen2

        assertEquals(564570, screen.articleId.articleId)
    }

    private fun intent(uri: Uri) = Intent(Intent.ACTION_VIEW, uri)
}