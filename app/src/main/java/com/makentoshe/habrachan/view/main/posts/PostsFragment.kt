package com.makentoshe.habrachan.view.main.posts

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.posts.PostsFragmentModule
import com.makentoshe.habrachan.di.main.posts.PostsFragmentScope
import com.makentoshe.habrachan.model.main.posts.PostModelFactory
import com.makentoshe.habrachan.model.main.posts.PostsEpoxyController
import com.makentoshe.habrachan.ui.main.posts.PostsFragmentUi
import com.makentoshe.habrachan.viewmodel.main.posts.ArticlesViewModel
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

    private val articlesViewModel by inject<ArticlesViewModel>()

    private val disposables = CompositeDisposable()
    val arguments = Arguments(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return PostsFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val controller = PostsEpoxyController(PostModelFactory(router))
        val progressbar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val messageView = view.findViewById<TextView>(R.id.error_message)

        val retryButton = view.findViewById<MaterialButton>(R.id.retry_button)
        retryButton.setOnClickListener {
            viewModel.newRequestObserver.onNext(Unit)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.main_posts_slidingpanel_recyclerview)
        recyclerView.adapter = controller.adapter

        val swipeRefresh = view.findViewById<SwipyRefreshLayout>(R.id.swipe_refresh_layout)
        swipeRefresh.onViewCreated(controller)

        val slidingUpPanelLayout = view.findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel)
        slidingUpPanelLayout.onViewCreated(view)


        viewModel.articleObservable.subscribe { articles ->
            retryButton.visibility = View.GONE
            progressbar.visibility = View.GONE
            swipeRefresh.visibility = View.VISIBLE
            swipeRefresh.isRefreshing = false

            val lastItem = controller.adapter.itemCount
            controller.append(articles)
            controller.requestModelBuild()
            if (arguments.page != 1) {
                val scroller = object : LinearSmoothScroller(view.context) {
                    override fun getVerticalSnapPreference() = SNAP_TO_START
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics) = .2f
                }
                scroller.targetPosition = lastItem
                recyclerView.layoutManager?.startSmoothScroll(scroller)
            }
            arguments.page += 1
        }.let(disposables::add)

        viewModel.progressObservable.subscribe {
            retryButton.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
            messageView.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }.let(disposables::add)

        viewModel.errorObservable.subscribe {
            progressbar.visibility = View.GONE
            swipeRefresh.isRefreshing = false
            if (arguments.page > 1) return@subscribe
            retryButton.visibility = View.VISIBLE
            messageView.text = it.toString()
            messageView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }.let(disposables::add)
    }

    private fun SlidingUpPanelLayout.onViewCreated(view: View) {
        isTouchEnabled = false
        val magnifyIcon = view.findViewById<View>(R.id.main_posts_toolbar_magnify)
        magnifyIcon.setOnClickListener {
            when (panelState) {
                SlidingUpPanelLayout.PanelState.EXPANDED -> {
                    panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                }
                SlidingUpPanelLayout.PanelState.COLLAPSED -> {
                    panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                    closeSoftKeyboard()
                }
                else -> Unit
            }
        }
    }

    private fun SwipyRefreshLayout.onViewCreated(controller: PostsEpoxyController) {
        setDistanceToTriggerSync(150)
        setOnRefreshListener { direction ->
            if (direction == SwipyRefreshLayoutDirection.TOP) {
                controller.clear()
                arguments.page = 1
                viewModel.newRequestObserver.onNext(Unit)
            }
            if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                viewModel.pageRequestObserver.onNext(arguments.page + 1)
            }
        }
    }

    private fun closeSoftKeyboard() {
        val imm = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {
        fun build(page: Int) = PostsFragment().apply {
            arguments.page = page
        }
    }

    class Arguments(fragment: PostsFragment) {

        init {
            (fragment as Fragment).arguments = Bundle()
        }

        private val fragmentArguments = fragment.requireArguments()

        var page: Int
            get() = fragmentArguments.getInt(PAGE)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }

    }

    private fun injectDependencies() {
        val module = PostsFragmentModule.Factory(this).build(arguments.page)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, PostsFragmentScope::class.java)
        scopes.closeOnDestroy(this).installModules(module).inject(this)
    }
}
