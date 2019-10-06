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
import com.makentoshe.habrachan.model.MainFlowBroadcastReceiver
import com.makentoshe.habrachan.model.posts.PostsFragmentViewPagerAdapter
import com.makentoshe.habrachan.ui.posts.PostsFragmentUi
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy
import java.text.FieldPosition

class PostsFragment : Fragment() {

    private val uiFactory by inject<PostsFragmentUi>()

    private var pageArg: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("Page", value)
        get() = arguments!!.getInt("Page")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val module = PostsFragmentModule()
        Toothpick.openScope(PostsFragmentScope::class.java).installModules(module).closeOnDestroy(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return uiFactory.createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = PostsFragmentViewPagerAdapter(childFragmentManager)
        initViewPager(adapter, pageArg)

        view.findViewById<View>(R.id.main_posts_toolbar_magnify).setOnClickListener {
            onMagnifyClicked()
        }
    }

    private fun onMagnifyClicked() {

    }

    private fun initViewPager(adapter: PostsFragmentViewPagerAdapter, initialPage: Int) {
        val viewpager = view!!.findViewById<ViewPager>(R.id.main_posts_viewpager)
        viewpager.adapter = adapter
        viewpager.currentItem = initialPage
        viewpager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageSelected(position: Int) = this@PostsFragment.onPageSelected(position)
        })
    }

    private fun onPageSelected(position: Int) {
        MainFlowBroadcastReceiver.sendBroadcast(requireContext(), position)
    }

    companion object {
        fun build(page: Int) = PostsFragment().apply {
            this.pageArg = page
        }
    }
}