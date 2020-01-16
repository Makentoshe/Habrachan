package com.makentoshe.habrachan.view.post.images

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val panelView = view.findViewById<SlidingUpPanelLayout>(R.id.post_image_fragment_panel)
        val textview = view.findViewById<TextView>(R.id.post_image_fragment_textview)
        val progressbar = view.findViewById<ProgressBar>(R.id.post_image_fragment_progressbar)
        val gifView = view.findViewById<GifImageView>(R.id.post_image_fragment_gifview)
        val imageView = view.findViewById<SubsamplingScaleImageView>(R.id.post_image_fragment_imageview)
        imageView.maxScale = 10f

        viewModel.bitmapObserver.subscribe {
            imageView.setImage(ImageSource.bitmap(it))
            imageView.visibility = View.VISIBLE
            progressbar.visibility = View.GONE
        }.let(disposables::add)

        viewModel.errorObserver.subscribe {
            it.printStackTrace()
            progressbar.visibility = View.GONE
            textview.text = it.toString()
            textview.visibility = View.VISIBLE
        }.let(disposables::add)

        viewModel.gifDrawableObserver.subscribe { gifDrawable ->
            gifView.setImageDrawable(gifDrawable)
            gifView.visibility = View.VISIBLE
            progressbar.visibility = View.GONE
            gifDrawable.start()
        }.let(disposables::add)

        // panel sliding control
        imageView.setOnStateChangedListener(SubsamplingImageStateListener(panelView, imageView))

        // panel display on slide event
        panelView.addPanelSlideListener(PanelSlideListener(navigator, panelView))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun injectDependencies() {
        val module = PostImageFragmentPageModule.Factory().build(this)
        val scopes =
            Toothpick.openScopes(ApplicationScope::class.java, PostImageFragmentPageScope::class.java, arguments.source)
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
        fun back() = router.backTo(PostScreen(-1))
    }

    private class SubsamplingImageStateListener(
        private val panelView: SlidingUpPanelLayout,
        private val imageView: SubsamplingScaleImageView
    ) : SubsamplingScaleImageView.DefaultOnStateChangedListener() {
        override fun onScaleChanged(newScale: Float, origin: Int) {
            panelView.isEnabled = imageView.minScale == newScale
            println("$newScale\t${imageView.minScale}")
        }
    }

    private class PanelSlideListener(
        private val navigator: Navigator,
        private val panelView: View
    ) : SlidingUpPanelLayout.PanelSlideListener {

        override fun onPanelSlide(panel: View, slideOffset: Float) {
            panelView.alpha = 1 - (1 - slideOffset) * 2
        }

        override fun onPanelStateChanged(
            panel: View, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState
        ) {
            if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                navigator.back()
            }
        }
    }

}
