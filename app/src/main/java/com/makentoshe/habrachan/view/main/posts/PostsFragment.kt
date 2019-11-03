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
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.di.main.posts.PostsFragmentScope
import com.makentoshe.habrachan.model.main.MainFlowBroadcastReceiver
import com.makentoshe.habrachan.model.main.posts.PostsBroadcastReceiver
import com.makentoshe.habrachan.model.main.posts.PostsFragmentViewPagerAdapter
import com.makentoshe.habrachan.ui.main.posts.PostsFragmentUi
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class PostsFragment : Fragment() {

    private val broadcastReceiver = PostsBroadcastReceiver()

    private var pageArg: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("Page", value)
        get() = arguments!!.getInt("Page")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return PostsFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initPanel()
        view.findViewById<View>(R.id.main_posts_toolbar_magnify).setOnClickListener {
            onMagnifyClicked()
        }
    }

    override fun onStart() {
        super.onStart()
        broadcastReceiver.register(requireActivity()).addOnReceiveListener {
        }
    }

    override fun onStop() {
        super.onStop()
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

    class Factory {
        fun build(page: Int) = PostsFragment().apply {
            this.pageArg = page
        }
    }

    private fun injectDependencies() {
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, PostsFragmentScope::class.java)
        scopes.closeOnDestroy(this).inject(this)
    }

}
