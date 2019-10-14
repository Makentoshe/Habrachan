package com.makentoshe.habrachan.view.main.posts

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.NO_ID
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.RequestStorage
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.di.main.posts.PostsFragmentModule
import com.makentoshe.habrachan.di.main.posts.PostsFragmentScope
import com.makentoshe.habrachan.model.main.MainFlowBroadcastReceiver
import com.makentoshe.habrachan.model.main.posts.PostsBroadcastReceiver
import com.makentoshe.habrachan.model.main.posts.PostsFragmentViewPagerAdapter
import com.makentoshe.habrachan.ui.main.posts.PostsFragmentUi
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class PostsFragment : Fragment() {

    private val broadcastReceiver by inject<PostsBroadcastReceiver>()

    private val uiFactory by inject<PostsFragmentUi>()

    private val requestFactory by inject<GetPostsRequestFactory>()

    private val requestStorage by inject<RequestStorage>()

    private var pageArg: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("Page", value)
        get() = arguments!!.getInt("Page")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
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
        broadcastReceiver.register(requireActivity()).addOnReceiveListener {
            initViewPager(adapter, pageArg)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        broadcastReceiver.unregister(requireActivity())
    }

    private fun onMagnifyClicked() = when (getPanelState()) {
        SlidingUpPanelLayout.PanelState.EXPANDED -> openPanel()
        SlidingUpPanelLayout.PanelState.COLLAPSED -> {
            closePanel()
            closeSoftKeyboard()
        }
        else -> Unit
    }

    private fun initPanel() {
        val panel = view!!.findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel)
        panel.isTouchEnabled = false

        if (isQueryPresents()) initQuerySearch()
    }

    private fun isQueryPresents(): Boolean {
        return requireView().findViewById<View>(R.id.main_posts_query) != null
    }

    private fun initQuerySearch() {
        initChipGroup()
        requireView().findViewById<View>(R.id.main_posts_query_search_button).setOnClickListener {
            onQuerySearchClicked()
        }
    }

    private fun initChipGroup() {
        val chipGroup = requireView().findViewById<ChipGroup>(R.id.main_posts_search_query_chipgroup)
        chipGroup.isSingleSelection = true
        chipGroup.check(R.id.main_posts_search_query_chip_relevance)
        chipGroup.findViewById<Chip>(chipGroup.checkedChipId).isClickable = false
        chipGroup.setOnCheckedChangeListener(QueryCheckedChangeListener())
    }

    private fun onQuerySearchClicked() {
        val query = requireView().findViewById<TextView>(R.id.main_posts_search_query_edittext)
        val queryString = query.text.toString()
        val chipGroup = requireView().findViewById<ChipGroup>(R.id.main_posts_search_query_chipgroup)
        val sort = when (chipGroup.checkedChipId) {
            R.id.main_posts_search_query_chip_relevance -> "relevance"
            else -> ""
        }
        val request = requestFactory.query(pageArg + 1, queryString, sort)
        requestStorage.set(request)
        PostsBroadcastReceiver.sendRefreshBroadcast(requireContext())
        closePanel()
        closeSoftKeyboard()
    }

    private fun getPanelState(): SlidingUpPanelLayout.PanelState {
        return requireView().findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel).panelState
    }

    private fun openPanel() {
        requireView().findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel).panelState =
            SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    private fun closePanel() {
        requireView().findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel).panelState =
            SlidingUpPanelLayout.PanelState.EXPANDED
    }

    private fun closeSoftKeyboard() {
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireActivity())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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

    private fun injectDependencies() {
        val module = PostsFragmentModule.Builder().build(this)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, PostsFragmentScope::class.java)
        scopes.installModules(module).inject(this)
        scopes.release()
    }

    private class QueryCheckedChangeListener : ChipGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: ChipGroup, checkedId: Int) {
            if (checkedId == NO_ID) return
            group.forEach { it.isClickable = true }
            group.findViewById<Chip>(checkedId).isClickable = false
        }
    }

}
