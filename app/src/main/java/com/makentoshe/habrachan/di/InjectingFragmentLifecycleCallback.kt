package com.makentoshe.habrachan.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.account.AccountFlowFragmentModule
import com.makentoshe.habrachan.di.main.account.AccountFlowFragmentScope
import com.makentoshe.habrachan.di.post.comments.CommentsFragmentModule
import com.makentoshe.habrachan.di.post.comments.CommentsFragmentScope
import com.makentoshe.habrachan.view.main.account.AccountFlowFragment
import com.makentoshe.habrachan.view.post.comments.CommentsFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class InjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        println("Attached: $f")
        when (f) {
            is CommentsFragment -> injectCommentsFragment(f)
            is AccountFlowFragment -> injectAccountFlowFragment(f)
        }
    }

    private fun injectCommentsFragment(fragment: CommentsFragment) {
        val module = CommentsFragmentModule.Factory().build(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, CommentsFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectAccountFlowFragment(fragment: AccountFlowFragment) {
        val module = AccountFlowFragmentModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, AccountFlowFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
}