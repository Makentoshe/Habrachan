package com.makentoshe.habrachan.view.posts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.posts.PostsFragmentModule
import com.makentoshe.habrachan.di.posts.PostsFragmentScope
import com.makentoshe.habrachan.model.posts.PostsFragmentViewPagerAdapter
import com.makentoshe.habrachan.ui.posts.PostsFragmentUi
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class PostsFragment : Fragment() {

    private val uiFactory by inject<PostsFragmentUi>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val module = PostsFragmentModule()
        Toothpick.openScope(PostsFragmentScope::class.java).installModules(module).closeOnDestroy(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return uiFactory.createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewpager = view.findViewById<ViewPager>(R.id.main_posts_viewpager)
        viewpager.adapter = PostsFragmentViewPagerAdapter(childFragmentManager)
    }
}