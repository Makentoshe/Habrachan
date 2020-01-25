package com.makentoshe.habrachan.view.main.posts

import android.animation.Animator
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

class PostsFragment : Fragment(), PostsFragmentArgumentsHolder {

    private val router by inject<Router>()
    private val viewModel by inject<PostsViewModel>()

    private val disposables = CompositeDisposable()

    override var page: Int
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
        val controller = PostsEpoxyController(PostModelFactory(router))

        val recyclerView = view.findViewById<RecyclerView>(R.id.main_posts_slidingpanel_recyclerview)
        RecyclerViewController(disposables, controller, recyclerView, this).install(viewModel)

        val swipeRefresh = view.findViewById<SwipyRefreshLayout>(R.id.swipe_refresh_layout)
        SwipeRefreshController(disposables, controller, swipeRefresh, this).install(viewModel)

        val progressbar = view.findViewById<ProgressBar>(R.id.progress_bar)
        ProgressBarController(disposables, progressbar).install(viewModel)

        val messageView = view.findViewById<TextView>(R.id.error_message)
        ErrorMessageController(this, disposables, messageView).install(viewModel)

        val slidingUpPanelLayout = view.findViewById<SlidingUpPanelLayout>(R.id.main_posts_slidingpanel)
        val magnifyIcon = view.findViewById<View>(R.id.main_posts_toolbar_magnify)
        SlidingPanelController(disposables, requireActivity(), slidingUpPanelLayout).install(viewModel, magnifyIcon)

        val retryButton = view.findViewById<MaterialButton>(R.id.retry_button)
        RetryButtonController(this, disposables, retryButton).install(viewModel)

        val topScrollButton = view.findViewById<MaterialButton>(R.id.scroll_to_top)
        TopFloatingButtonController(topScrollButton).install(recyclerView)
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
        private val holder: PostsFragmentArgumentsHolder,
        private val disposables: CompositeDisposable,
        private val messageView: TextView
    ) {

        fun install(viewModel: PostsViewModel) {
            viewModel.errorObservable.subscribe {
                if (holder.page > 1) return@subscribe
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
        private val holder: PostsFragmentArgumentsHolder,
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
                if (holder.page > 1) return@subscribe
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
        private val holder: PostsFragmentArgumentsHolder
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
                holder.page += 1
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
            holder.page = 1
            viewModel.newRequestObserver.onNext(holder.page)
        }

        private fun onBotRefresh(viewModel: PostsViewModel) {
            viewModel.requestObserver.onNext(holder.page + 1)
        }
    }

    class RecyclerViewController(
        private val disposables: CompositeDisposable,
        private val controller: PostsEpoxyController,
        private val view: RecyclerView,
        private val holder: PostsFragmentArgumentsHolder
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
            if (holder.page != 1) {
                scrollToPageDivider(lastItem)
            }
        }

        private fun scrollToPageDivider(lastItem: Int) {
            val scroller = this.scroller
            scroller.targetPosition = lastItem
            layoutManager.startSmoothScroll(scroller)
        }
    }

    class TopFloatingButtonController(private val button: MaterialButton) {

        private val ANIM_STATE_IDLE = 0
        private val ANIM_STATE_HIDING = 1
        private val ANIM_STATE_SHOWING = 2

        private var state = ANIM_STATE_IDLE
        private var isStateLocked = false

        fun install(recyclerView: RecyclerView) {
            button.setOnClickListener {
                recyclerView.smoothScrollToPosition(0)
                button.hide()
            }
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    isStateLocked = newState == RecyclerView.SCROLL_STATE_SETTLING
                }
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    onScrolled(recyclerView, dy)
                }
            })
        }

        private fun onScrolled(recyclerView: RecyclerView, dy: Int) {
            if (dy > 0 || recyclerView.computeVerticalScrollOffset() <= 100) {
                button.hide()
            }
            if (dy < 0 && !isStateLocked) {
                button.show()
            }
        }

        private fun MaterialButton.hide() {
            // skip call if we already hiding
            if (state != ANIM_STATE_IDLE) return
            state = ANIM_STATE_HIDING
            animate().scaleX(0f).scaleY(0f).setDuration(400)
                .setListener(object : DefaultAnimator() {
                    override fun onAnimationEnd(animation: Animator?) {
                        visibility = View.GONE
                        state = ANIM_STATE_IDLE
                    }
                }).start()
        }

        private fun MaterialButton.show() {
            // skip call if we already showing
            if (state != ANIM_STATE_IDLE) return
            state = ANIM_STATE_SHOWING
            visibility = View.VISIBLE
            animate().scaleX(1f).scaleY(1f).setDuration(400)
                .setListener(object : DefaultAnimator() {
                    override fun onAnimationEnd(animation: Animator?) {
                        state = ANIM_STATE_IDLE
                    }
                }).start()
        }

        private abstract class DefaultAnimator : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) = Unit
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) = Unit
            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) = Unit
        }
    }
}
