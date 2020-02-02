package com.makentoshe.habrachan.di

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.di.post.comments.CommentsFragmentModule
import com.makentoshe.habrachan.di.post.comments.CommentsFragmentScope
import com.makentoshe.habrachan.view.post.comments.CommentsFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class InjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        println("Created: $f")
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        println("Activity: $f")
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        println("Attached: $f")
    }

    private fun injectCommentsFragment(fragment: CommentsFragment) {
        val module = CommentsFragmentModule.Factory().build(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, CommentsFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
}