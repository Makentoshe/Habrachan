package com.makentoshe.habrachan.view.main.posts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.di.main.posts.PostsFragmentModule
import com.makentoshe.habrachan.di.main.posts.PostsFragmentScope
import com.makentoshe.habrachan.model.main.posts.PostModelFactory
import com.makentoshe.habrachan.model.main.posts.PostsEpoxyController
import com.makentoshe.habrachan.ui.main.posts.PostsFragmentUi
import com.makentoshe.habrachan.viewmodel.main.posts.PostsViewModel
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class PostsFragment : Fragment() {

    private val router by inject<Router>()
    private val viewModel by inject<PostsViewModel>()

    private val disposables = CompositeDisposable()

    private var page: Int
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
        initSwipeRefreshLayout()
        initRecyclerView()

        view.findViewById<View>(R.id.main_posts_toolbar_magnify).setOnClickListener {
            onMagnifyClicked()
        }
    }

    private fun initSwipeRefreshLayout() {
        val view = requireView().findViewById<SwipyRefreshLayout>(R.id.swipe_refresh_layout)
        view.setDistanceToTriggerSync(150)
        view.setOnRefreshListener {
            if (it == SwipyRefreshLayoutDirection.TOP) {
                return@setOnRefreshListener
            }
            if (it == SwipyRefreshLayoutDirection.BOTTOM) {
                viewModel.requestPosts(page++)
                return@setOnRefreshListener
            }
        }
        // disable refreshing on each event
        val errors = viewModel.errorObservable.map { false }
        val successes = viewModel.postsObservable.map { false }
        successes.mergeWith(errors).subscribe(view::setRefreshing).let(disposables::add)
    }

    private fun initRecyclerView() {
        val modelFactory = PostModelFactory(router)
        val controller = PostsEpoxyController(modelFactory)
        val layoutManager = LinearLayoutManager(requireContext())
        val view = requireView().findViewById<RecyclerView>(R.id.main_posts_slidingpanel_recyclerview)
        view.adapter = controller.adapter
        view.layoutManager = layoutManager

        viewModel.postsObservable.subscribe {
            val lastItem = controller.adapter.itemCount
            controller.append(it)
            controller.requestModelBuild()
            if (page != 1) {
                val scroller = object : LinearSmoothScroller(requireContext()) {
                    override fun getVerticalSnapPreference() = SNAP_TO_START
                }
                scroller.targetPosition = lastItem
                layoutManager.startSmoothScroll(scroller)
            }
            page = page.inc()
        }.let(disposables::add)
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
        val imm = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireActivity())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    class Factory {
        fun build(page: Int) = PostsFragment().apply {
            this.page = page
        }
    }

    private fun injectDependencies() {
        val module = PostsFragmentModule.Factory(this).build(page)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, PostsFragmentScope::class.java)
        scopes.closeOnDestroy(this).installModules(module).inject(this)
    }
}
