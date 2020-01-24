package com.makentoshe.habrachan.view.post.images

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.di.post.images.PostImageFragmentPageModule
import com.makentoshe.habrachan.di.post.images.PostImageFragmentPageScope
import com.makentoshe.habrachan.model.post.PostScreen
import com.makentoshe.habrachan.ui.post.images.PostImageFragmentPageUi
import com.makentoshe.habrachan.viewmodel.post.images.PostImageFragmentViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

/* Single page for resource detail view such as images or svg files */
class PostImageFragmentPage : Fragment() {

    private val disposables = CompositeDisposable()
    val arguments = Arguments(this)

    private val viewModel by inject<PostImageFragmentViewModel>()

    /** Creates in [PostImageFragmentPageModule] and injects during onAttach lifecycle event */
    private val navigator by inject<Navigator>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PostImageFragmentPageUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val views = CreatedViews(view)

        viewModel.bitmapObserver.subscribe(OnBitmapSuccess(views)).let(disposables::add)
        viewModel.errorObserver.subscribe(OnError(views)).let(disposables::add)
        viewModel.gifDrawableObserver.subscribe(OnGifDrawableSuccess(views)).let(disposables::add)
        viewModel.progressObserver.subscribe(OnProgress(views)).let(disposables::add)
        // panel sliding control
        views.imageView.setOnStateChangedListener(SubsamplingImageStateListener(views))
        // panel alpha display on slide event
        views.panelView.addPanelSlideListener(PanelSlideListener(navigator, views))
        // retries download
        views.retryButton.setOnClickListener(RetryOnClickListener(viewModel))
        // avoid panel collapse on views click
        views.messageView.setOnClickListener {}
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun injectDependencies() {
        val module = PostImageFragmentPageModule.Factory().build(this)
        val scopes = Toothpick.openScopes(
            ApplicationScope::class.java, PostImageFragmentPageScope::class.java
        )
        scopes.closeOnDestroy(this).installModules(module).inject(this)
        Toothpick.closeScope(scopes)
    }

    class Factory {
        fun build(source: String): PostImageFragmentPage {
            val fragment = PostImageFragmentPage()
            fragment.arguments.source = source
            return fragment
        }
    }

    class Arguments(fragment: Fragment) {

        init {
            fragment.arguments = Bundle()
        }

        private val fragmentArguments = fragment.requireArguments()

        var source: String
            get() = fragmentArguments.getString(SOURCE) ?: ""
            set(value) = fragmentArguments.putString(SOURCE, value)

        companion object {
            private const val SOURCE = "Source"
        }
    }

    /** For navigations from [PostImageFragmentPage] */
    class Navigator(private val router: Router) {
        /**
         * Using [Router.backTo] because default [Router.exit] also closes [PostScreen]
         * and returns to [MainFlowScreen]. No matter what arguments we pass to [PostScreen]:
         * it will be found in backstack and displayed without problems
         */
        fun back() = router.exit()
    }

    private class SubsamplingImageStateListener(
        private val views: CreatedViews
    ) : SubsamplingScaleImageView.DefaultOnStateChangedListener() {
        override fun onScaleChanged(newScale: Float, origin: Int) {
            views.panelView.isEnabled = views.imageView.minScale == newScale
        }
    }

    // TODO fix urls after rotation
    private class PanelSlideListener(
        private val navigator: Navigator, private val views: CreatedViews
    ) : SlidingUpPanelLayout.SimplePanelSlideListener() {

        private var lock = false

        override fun onPanelSlide(panel: View, slideOffset: Float) {
            views.panelView.alpha = 1 - (1 - slideOffset) * 2
            if (slideOffset <= 0.2 && !lock) {
                lock = true
                navigator.back()
            }
        }
    }

    private class RetryOnClickListener(
        private val viewModel: PostImageFragmentViewModel
    ) : View.OnClickListener {
        override fun onClick(v: View) {
            viewModel.startLoad()
        }
    }

    private class CreatedViews(view: View) {
        val panelView = view.findViewById<SlidingUpPanelLayout>(R.id.post_image_fragment_panel)!!
        val messageView = view.findViewById<TextView>(R.id.post_image_fragment_textview)!!
        val retryButton = view.findViewById<Button>(R.id.post_image_fragment_retrybutton)!!
        val progressbar = view.findViewById<ProgressBar>(R.id.post_image_fragment_progressbar)!!
        val gifView = view.findViewById<GifImageView>(R.id.post_image_fragment_gifview)!!
        val imageView = view.findViewById<SubsamplingScaleImageView>(R.id.post_image_fragment_imageview)!!

        init {
            imageView.maxScale = 10f
        }
    }

    private class OnProgress(private val views: CreatedViews) : Consumer<Unit> {
        override fun accept(t: Unit) {
            views.messageView.visibility = View.GONE
            views.progressbar.visibility = View.VISIBLE
            views.retryButton.visibility = View.GONE
        }
    }

    private class OnError(private val views: CreatedViews) : Consumer<Throwable> {
        override fun accept(it: Throwable) {
            views.progressbar.visibility = View.GONE
            views.messageView.text = it.toString()
            views.messageView.visibility = View.VISIBLE
            views.retryButton.visibility = View.VISIBLE
        }
    }

    private class OnBitmapSuccess(private val views: CreatedViews) : Consumer<Bitmap> {
        override fun accept(it: Bitmap) {
            views.imageView.setImage(ImageSource.bitmap(it))
            views.imageView.visibility = View.VISIBLE
            views.progressbar.visibility = View.GONE
            views.retryButton.visibility = View.GONE
        }
    }

    private class OnGifDrawableSuccess(private val views: CreatedViews): Consumer<GifDrawable> {
        override fun accept(it: GifDrawable) {
            views.gifView.setImageDrawable(it)
            views.gifView.visibility = View.VISIBLE
            views.progressbar.visibility = View.GONE
            views.retryButton.visibility = View.GONE
            it.start()
        }
    }

}
