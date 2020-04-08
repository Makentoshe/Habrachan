package com.makentoshe.habrachan.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.di.article.ArticleFragmentModule
import com.makentoshe.habrachan.di.article.ArticleFragmentScope
import com.makentoshe.habrachan.di.comments.CommentsFragmentModule
import com.makentoshe.habrachan.di.comments.CommentsFragmentScope
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.MainFlowFragmentModule
import com.makentoshe.habrachan.di.main.MainFlowFragmentScope
import com.makentoshe.habrachan.di.main.account.login.LoginFragmentModule
import com.makentoshe.habrachan.di.main.account.login.LoginFragmentScope
import com.makentoshe.habrachan.di.main.articles.ArticlesFragmentModule
import com.makentoshe.habrachan.di.main.articles.ArticlesFragmentScope
import com.makentoshe.habrachan.di.user.UserFragmentModule
import com.makentoshe.habrachan.di.user.UserFragmentScope
import com.makentoshe.habrachan.view.article.ArticleFragment
import com.makentoshe.habrachan.view.comments.CommentsFragment
import com.makentoshe.habrachan.view.main.MainFlowFragment
import com.makentoshe.habrachan.view.main.login.LoginFragment
import com.makentoshe.habrachan.view.main.articles.ArticlesFragment
import com.makentoshe.habrachan.view.user.UserFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class InjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        println("Attached: $f")
        when (f) {
            is MainFlowFragment -> injectMainFlowFragment(f)
            is CommentsFragment -> injectCommentsFragment(f)
            is LoginFragment -> injectLoginFragment(f)
            is UserFragment -> injectUserFragment(f)
            is ArticlesFragment -> injectArticlesFragment(f)
            is ArticleFragment -> injectArticleFragment(f)
        }
    }

    private fun injectMainFlowFragment(fragment: MainFlowFragment) {
        val module = MainFlowFragmentModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, MainFlowFragmentScope::class.java)
        scope.installModules(module).closeOnDestroy(fragment).inject(fragment)
    }

    private fun injectCommentsFragment(fragment: CommentsFragment) {
        val module = CommentsFragmentModule.Factory().build(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, CommentsFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectLoginFragment(fragment: LoginFragment) {
        val module = LoginFragmentModule(fragment)
        val scope = Toothpick.openScopes(MainFlowFragmentScope::class.java, LoginFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectUserFragment(fragment: UserFragment) {
        val module = UserFragmentModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, UserFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectArticlesFragment(fragment: ArticlesFragment) {
        val module = ArticlesFragmentModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, ArticlesFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectArticleFragment(fragment: ArticleFragment) {
        val module = ArticleFragmentModule(fragment)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, ArticleFragmentScope::class.java)
        scopes.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
}