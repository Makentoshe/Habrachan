package com.makentoshe.habrachan.application.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.broadcast.ApplicationStateBroadcastReceiver
import com.makentoshe.habrachan.application.android.navigation.StackSupportAppNavigator
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleScreen
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesFlowScreen
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsScreen
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.articleId
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject

// TODO - rework resources access in the modules
class AppActivity : AppCompatActivity() {

    private val navigator = StackSupportAppNavigator(this, supportFragmentManager, R.id.fragment_main_container)

    private val navigatorHolder by inject<NavigatorHolder>()
    private val router by inject<Router>()
    private val applicationStateBroadcastReceiver by inject<ApplicationStateBroadcastReceiver>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) return

        when (intent.action) {
            Intent.ACTION_MAIN -> defaultStart(intent)
            Intent.ACTION_VIEW -> deeplinkStart(intent)
        }
    }

    private fun defaultStart(intent: Intent) {
        val screen = ArticlesFlowScreen()
        router.newRootScreen(screen)
    }

    private fun deeplinkStart(intent: Intent) {
        val uri = intent.data ?: throw IllegalStateException("wtf?")
        when (uri.pathSegments.first()) {
            "post" -> deeplinkArticleStart(articleId(uri.pathSegments[1].toInt()))
            else -> deeplinkOtherStart(uri)
        }
    }

    private fun deeplinkOtherStart(uri: Uri) {
        println(uri.pathSegments)
        when (val second = uri.pathSegments[1]) {
            "post" -> deeplinkArticleStart(articleId(uri.pathSegments[2].toInt()))
            "company" -> deeplinkCorporateStart(uri)
            "users" -> deeplinkUserStart(uri.pathSegments[2])
        }
    }

    private fun deeplinkUserStart(login: String) {
        router.newRootScreen(UserScreen(UserAccount.User(login)))
    }

    private fun deeplinkCorporateStart(uri: Uri) {
//        val companyName = uri.pathSegments[2]
        val articleId = articleId(uri.pathSegments[4].toInt())
        when (uri.pathSegments.last()) {
            "comments" -> deeplinkCommentsStart(articleId)
            else -> deeplinkArticleStart(articleId)
        }
    }

    private fun deeplinkCommentsStart(articleId: ArticleId) {
        router.newRootScreen(ArticleCommentsScreen(articleId, ""))
    }

    private fun deeplinkArticleStart(articleId: ArticleId) {
        router.newRootScreen(ArticleScreen(articleId))
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(applicationStateBroadcastReceiver, ApplicationStateBroadcastReceiver.filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(applicationStateBroadcastReceiver)
    }

}
