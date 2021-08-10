import android.content.Intent
import android.net.Uri
import com.makentoshe.habrachan.application.android.Launcher
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesFlowScreen
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsScreen
import com.makentoshe.habrachan.application.android.screen.comments.navigation.DiscussionCommentsScreen
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LauncherTest : TestCase() {

    private val launcher = Launcher(ArticlesFlowScreen())

    @Test
    fun testShouldOpenDiscussionCommentsScreen1() {
        val intent = intent(Uri.parse("https://habr.com/en/post/571418/#comment_23344290"))
        val screen = launcher.deeplink(intent) as DiscussionCommentsScreen

        assertEquals(571418, screen.articleId)
        assertEquals(23344290, screen.commentId)
    }

    @Test
    fun testShouldOpenDiscussionCommentsScreen2() {
        val intent = intent(Uri.parse("https://habr.com/en/post/571418/comments/#comment_23344290"))
        val screen = launcher.deeplink(intent) as DiscussionCommentsScreen

        assertEquals(571418, screen.articleId)
        assertEquals(23344290, screen.commentId)
    }

    @Test
    fun testShouldOpenArticleCommentsScreen() {
        val intent = intent(Uri.parse("https://habr.com/en/post/571418/comments/"))
        val screen = launcher.deeplink(intent) as ArticleCommentsScreen

        assertEquals(571418, screen.articleId.articleId)
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

    private fun intent(uri: Uri) = Intent(Intent.ACTION_VIEW, uri)
}