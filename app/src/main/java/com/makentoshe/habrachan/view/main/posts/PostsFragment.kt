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
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.Article
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

        val recyclerView = view.findViewById<RecyclerView>(R.id.main_posts_slidingpanel_recyclerview)
        RecyclerViewController(disposables, controller, recyclerView, arguments).install(viewModel)

        val swipeRefresh = view.findViewById<SwipyRefreshLayout>(R.id.swipe_refresh_layout)
        SwipeRefreshController(disposables, controller, swipeRefresh, arguments).install(viewModel)

        val progressbar = view.findViewById<ProgressBar>(R.id.progress_bar)
        ProgressBarController(disposables, progressbar).install(viewModel)

        val messageView = view.findViewById<TextView>(R.id.error_message)
        ErrorMessageController(arguments, disposables, messageView).install(viewModel)

        val slidingUpPanelLayout = view.findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel)
        val magnifyIcon = view.findViewById<View>(R.id.main_posts_toolbar_magnify)
        SlidingPanelController(disposables, requireActivity(), slidingUpPanelLayout).install(viewModel, magnifyIcon)

        val retryButton = view.findViewById<MaterialButton>(R.id.retry_button)
        RetryButtonController(arguments, disposables, retryButton).install(viewModel)
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

    class ProgressBarController(
        private val disposables: CompositeDisposable, private val progressbar: ProgressBar
    ) {

        fun install(viewModel: PostsViewModel) {
            val errors = viewModel.errorObservable.map { View.GONE }
            val successes = viewModel.postsObservable.map { View.GONE }
            successes.mergeWith(errors).subscribe(progressbar::setVisibility).let(disposables::add)

            viewModel.progressObservable.subscribe {
                progressbar.visibility = View.VISIBLE
            }.let(disposables::add)
        }
    }

    class ErrorMessageController(
        private val arguments: Arguments,
        private val disposables: CompositeDisposable,
        private val messageView: TextView
    ) {

        fun install(viewModel: PostsViewModel) {
            viewModel.errorObservable.subscribe {
                if (arguments.page > 1) return@subscribe
                messageView.text = it.toString()
                messageView.visibility = View.VISIBLE
            }.let(disposables::add)

            viewModel.progressObservable.subscribe {
                messageView.visibility = View.GONE
            }.let(disposables::add)
        }
    }

    class SlidingPanelController(
        private val disposables: CompositeDisposable,
        private val activity: FragmentActivity,
        private val panel: SlidingUpPanelLayout
    ) {

        init {
            panel.isTouchEnabled = false
        }

        fun install(viewModel: PostsViewModel, triggerView: View) {
            triggerView.setOnClickListener { onMagnifyClicked() }
        }

        private fun onMagnifyClicked() = when (panel.panelState) {
            SlidingUpPanelLayout.PanelState.EXPANDED -> openPanel()
            SlidingUpPanelLayout.PanelState.COLLAPSED -> {
                closePanel()
                closeSoftKeyboard()
            }
            else -> Unit
        }

        private fun openPanel() {
            panel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }

        private fun closePanel() {
            panel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        }

        private fun closeSoftKeyboard() {
            val imm = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = activity.currentFocus ?: View(activity)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    class RetryButtonController(
        private val arguments: PostsFragment.Arguments,
        private val disposables: CompositeDisposable,
        private val button: MaterialButton
    ) {

        fun install(viewModel: PostsViewModel) {
            viewModel.postsObservable.subscribe {
                button.visibility = View.GONE
            }.let(disposables::add)

            viewModel.progressObservable.subscribe {
                button.visibility = View.GONE
            }.let(disposables::add)

            viewModel.errorObservable.subscribe {
                if (arguments.page > 1) return@subscribe
                button.visibility = View.VISIBLE
            }.let(disposables::add)

            button.setOnClickListener {
                viewModel.newRequestObserver.onNext(1)
            }
        }
    }

    class SwipeRefreshController(
        private val disposables: CompositeDisposable,
        private val controller: PostsEpoxyController,
        private val view: SwipyRefreshLayout,
        private val arguments: PostsFragment.Arguments
    ) {

        init {
            view.setDistanceToTriggerSync(150)
        }

        fun install(viewModel: PostsViewModel) {
            view.setOnRefreshListener {
                onRefresh(viewModel, it)
            }

            viewModel.errorObservable.subscribe {
                view.isRefreshing = false
            }.let(disposables::add)

            viewModel.postsObservable.subscribe {
                arguments.page += 1
                view.visibility = View.VISIBLE
                view.isRefreshing = false
            }.let(disposables::add)
        }

        private fun onRefresh(viewModel: PostsViewModel, direction: SwipyRefreshLayoutDirection) {
            if (direction == SwipyRefreshLayoutDirection.TOP) {
                return onTopRefresh(viewModel)
            }
            if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                return onBotRefresh(viewModel)
            }
        }

        private fun onTopRefresh(viewModel: PostsViewModel) {
            controller.clear()
            arguments.page = 1
            viewModel.newRequestObserver.onNext(arguments.page)
        }

        private fun onBotRefresh(viewModel: PostsViewModel) {
            viewModel.requestObserver.onNext(arguments.page + 1)
        }
    }

    class RecyclerViewController(
        private val disposables: CompositeDisposable,
        private val controller: PostsEpoxyController,
        private val view: RecyclerView,
        private val arguments: PostsFragment.Arguments
    ) {

        private val layoutManager = LinearLayoutManager(view.context)
        private val scroller = object : LinearSmoothScroller(view.context) {
            override fun getVerticalSnapPreference() = SNAP_TO_START
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics) = .2f
        }

        fun install(viewModel: PostsViewModel) {
            view.adapter = controller.adapter
            view.layoutManager = layoutManager

            viewModel.postsObservable.subscribe(::onSuccess).let(disposables::add)
        }

        private fun onSuccess(posts: List<Article>) {
            val lastItem = controller.adapter.itemCount
            controller.append(posts)
            controller.requestModelBuild()
            if (arguments.page != 1) {
                scrollToPageDivider(lastItem)
            }
        }

        private fun scrollToPageDivider(lastItem: Int) {
            val scroller = this.scroller
            scroller.targetPosition = lastItem
            layoutManager.startSmoothScroll(scroller)
        }
    }
}
