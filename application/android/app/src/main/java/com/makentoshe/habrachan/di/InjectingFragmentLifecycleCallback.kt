package com.makentoshe.habrachan.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.screen.image.OverlayImageFragment
import com.makentoshe.habrachan.application.android.screen.image.di.OverlayImageFragmentModule
import com.makentoshe.habrachan.application.android.screen.image.di.OverlayImageFragmentScope
import com.makentoshe.habrachan.di.comments.CommentsFragmentModule
import com.makentoshe.habrachan.di.comments.CommentsFragmentScope
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.login.*
import com.makentoshe.habrachan.di.user.UserArticlesFragmentModule
import com.makentoshe.habrachan.di.user.UserArticlesFragmentScope
import com.makentoshe.habrachan.di.user.UserFragmentModule
import com.makentoshe.habrachan.di.user.UserFragmentScope
import com.makentoshe.habrachan.view.comments.CommentsFragment
import com.makentoshe.habrachan.view.main.login.LoginFlowFragment
import com.makentoshe.habrachan.view.main.login.LoginFragment
import com.makentoshe.habrachan.view.main.login.OauthFragment
import com.makentoshe.habrachan.view.user.UserArticlesFragment
import com.makentoshe.habrachan.view.user.UserFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class InjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        println("Attached: $f")
        when (f) {
            is OverlayImageFragment -> injectOverlayImageFragment(f)

            is CommentsFragment -> injectCommentsFragment(f)

            is LoginFragment -> injectLoginFragment(f)
            is LoginFlowFragment -> injectLoginFlowFragment(f)
            is OauthFragment -> injectOauthFragment(f)

            is UserFragment -> injectUserFragment(f)

            is UserArticlesFragment -> injectUserArticlesFragment(f)
        }
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

    private fun injectUserArticlesFragment(fragment: UserArticlesFragment) {
        val module = UserArticlesFragmentModule(fragment)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, UserArticlesFragmentScope::class.java)
        scopes.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectOverlayImageFragment(fragment: OverlayImageFragment) {
        val module = OverlayImageFragmentModule(fragment)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, OverlayImageFragmentScope::class.java)
        scopes.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectOauthFragment(fragment: OauthFragment) {
        val module = OauthFragmentModule(fragment)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, OauthFragmentScope::class.java)
        scopes.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        println("Destroyed: $f")
    }
}