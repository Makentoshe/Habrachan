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
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

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
        initPanel()
        val adapter = PostsFragmentViewPagerAdapter(childFragmentManager)
        initViewPager(adapter, pageArg)

        view.findViewById<View>(R.id.main_posts_toolbar_magnify).setOnClickListener {
            onMagnifyClicked()
        }

        view.findViewById<View>(R.id.main_posts_query_search_button).setOnClickListener {
            closePanel()
        }
    }

    private fun onMagnifyClicked() = when (getPanelState()) {
        SlidingUpPanelLayout.PanelState.EXPANDED -> openPanel()
        SlidingUpPanelLayout.PanelState.COLLAPSED -> closePanel()
        else -> Unit
    }

    private fun initPanel() {
        val panel = view!!.findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel)
        panel.isTouchEnabled = false
    }

    private fun getPanelState(): SlidingUpPanelLayout.PanelState {
        return view!!.findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel).panelState
    }

    private fun openPanel() {
        view!!.findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel).panelState =
            SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    private fun closePanel() {
        view!!.findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel).panelState =
            SlidingUpPanelLayout.PanelState.EXPANDED
    }

    private fun initViewPager(adapter: PostsFragmentViewPagerAdapter, initialPage: Int) {
        val viewpager = view!!.findViewById<ViewPager>(R.id.main_posts_viewpager)
        viewpager.adapter = adapter
        viewpager.currentItem = initialPage
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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