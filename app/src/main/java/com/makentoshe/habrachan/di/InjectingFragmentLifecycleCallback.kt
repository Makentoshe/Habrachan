package com.makentoshe.habrachan.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.di.article.ArticleFragmentModule
import com.makentoshe.habrachan.di.article.ArticleFragmentScope
import com.makentoshe.habrachan.di.article.WebArticleFragmentModule
import com.makentoshe.habrachan.di.article.WebArticleFragmentScope
import com.makentoshe.habrachan.di.comments.CommentsFragmentModule
import com.makentoshe.habrachan.di.comments.CommentsFragmentScope
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.MainFlowFragmentModule
import com.makentoshe.habrachan.di.main.MainFlowFragmentScope
import com.makentoshe.habrachan.di.main.articles.*
import com.makentoshe.habrachan.di.main.login.LoginFlowFragmentModule
import com.makentoshe.habrachan.di.main.login.LoginFlowFragmentScope
import com.makentoshe.habrachan.di.main.login.LoginFragmentModule
import com.makentoshe.habrachan.di.main.login.LoginFragmentScope
import com.makentoshe.habrachan.di.user.UserArticlesFragmentModule
import com.makentoshe.habrachan.di.user.UserArticlesFragmentScope
import com.makentoshe.habrachan.di.user.UserFragmentModule
import com.makentoshe.habrachan.di.user.UserFragmentScope
import com.makentoshe.habrachan.view.article.ArticleFragment
import com.makentoshe.habrachan.view.article.WebArticleFragment
import com.makentoshe.habrachan.view.comments.CommentsFragment
import com.makentoshe.habrachan.view.main.MainFlowFragment
import com.makentoshe.habrachan.view.main.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.view.main.articles.ArticlesFragment
import com.makentoshe.habrachan.view.main.articles.ArticlesSearchFragment
import com.makentoshe.habrachan.view.main.login.LoginFlowFragment
import com.makentoshe.habrachan.view.main.login.LoginFragment
import com.makentoshe.habrachan.view.user.UserArticlesFragment
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
            is ArticlesFlowFragment -> injectArticlesFlowFragment(f)
            is ArticlesFragment -> injectArticlesFragment(f)
            is ArticlesSearchFragment -> injectArticlesSearchFragment(f)
            is WebArticleFragment -> injectWebArticleFragment(f)
            is UserArticlesFragment -> injectUserArticlesFragment(f)
            is LoginFlowFragment -> injectLoginFlowFragment(f)
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

    private fun injectLoginFlowFragment(fragment: LoginFlowFragment) {
        val module = LoginFlowFragmentModule(fragment)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, LoginFlowFragmentScope::class.java)
        scopes.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectLoginFragment(fragment: LoginFragment) {
        val module = LoginFragmentModule(fragment)
        val scope = Toothpick.openScopes(LoginFlowFragmentScope::class.java, LoginFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectUserFragment(fragment: UserFragment) {
        val module = UserFragmentModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, UserFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectArticlesFlowFragment(fragment: ArticlesFlowFragment) {
        val module = ArticlesFlowFragmentModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, ArticlesFlowFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectArticlesFragment(fragment: ArticlesFragment) {
        val module = ArticlesFragmentModule(fragment)
        val scope = Toothpick.openScopes(ArticlesFlowFragmentScope::class.java, ArticlesFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectArticlesSearchFragment(fragment: ArticlesSearchFragment) {
        val scope = Toothpick.openScopes(ArticlesFlowFragmentScope::class.java, ArticlesSearchFragmentScope::class.java)
        scope.closeOnDestroy(fragment).inject(fragment)
    }

    private fun injectWebArticleFragment(fragment: WebArticleFragment) {
        val articleModule = ArticleFragmentModule(fragment)
        val webArticleModule = WebArticleFragmentModule(fragment)
        Toothpick.openScopes(
            ApplicationScope::class.java, ArticleFragmentScope::class.java, WebArticleFragmentScope::class.java
        ).closeOnDestroy(fragment).installModules(articleModule, webArticleModule).inject(fragment)
    }

    private fun injectUserArticlesFragment(fragment: UserArticlesFragment) {
        val module = UserArticlesFragmentModule(fragment)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, UserArticlesFragmentScope::class.java)
        scopes.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
}