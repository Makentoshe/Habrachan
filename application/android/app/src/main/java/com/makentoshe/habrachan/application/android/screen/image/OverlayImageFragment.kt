package com.makentoshe.habrachan.application.android.screen.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.image.navigation.OverlayImageFragmentNavigation
import com.makentoshe.habrachan.application.android.screen.image.viewmodel.OverlayImageFragmentViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_image.*
import toothpick.ktp.delegate.inject

class OverlayImageFragment : Fragment() {

    companion object {
        fun build(source: String) = OverlayImageFragment().apply {
            arguments.source = source
        }
    }

    val arguments = Arguments(this)

    private val viewModel by inject<OverlayImageFragmentViewModel>()
    private val disposables by inject<CompositeDisposable>()
    private val navigator by inject<OverlayImageFragmentNavigation>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_image_image.maxScale = 10f

        fragment_image_retry.setOnClickListener {
            viewModel.startLoad()
        }

        viewModel.bitmapObserver.observeOn(AndroidSchedulers.mainThread()).subscribe {
            fragment_image_image.setImage(ImageSource.bitmap(it))
            fragment_image_image.visibility = View.VISIBLE
            fragment_image_progress.visibility = View.GONE
            fragment_image_retry.visibility = View.GONE
        }.let(disposables::add)

        viewModel.errorObserver.observeOn(AndroidSchedulers.mainThread()).subscribe {
            fragment_image_progress.visibility = View.GONE
            fragment_image_message.text = it.toString()
            fragment_image_message.visibility = View.VISIBLE
            fragment_image_retry.visibility = View.VISIBLE
        }.let(disposables::add)

        viewModel.progressObserver.observeOn(AndroidSchedulers.mainThread()).subscribe {
            fragment_image_message.visibility = View.GONE
            fragment_image_progress.visibility = View.VISIBLE
            fragment_image_retry.visibility = View.GONE
        }.let(disposables::add)

        viewModel.gifDrawableObserver.observeOn(AndroidSchedulers.mainThread()).subscribe {
            fragment_image_gif.setImageDrawable(it)
            fragment_image_gif.visibility = View.VISIBLE
            fragment_image_progress.visibility = View.GONE
            fragment_image_retry.visibility = View.GONE
            it.start()
        }.let(disposables::add)

        fragment_image_image.setOnStateChangedListener(object : SubsamplingScaleImageView.DefaultOnStateChangedListener() {
            override fun onScaleChanged(newScale: Float, origin: Int) {
                fragment_image_panel.isEnabled = fragment_image_image.minScale == newScale
            }
        })

        fragment_image_panel.addPanelSlideListener(object : SlidingUpPanelLayout.SimplePanelSlideListener() {
            var lock = false
            override fun onPanelSlide(panel: View, slideOffset: Float) {
                fragment_image_panel.alpha = 1 - (1 - slideOffset) * 2
                if (slideOffset <= 0.2 && !lock) {
                    lock = true
                    navigator.back()
                }
            }
        })
    }

    class Arguments(private val fragment: OverlayImageFragment) {

        init {
            val fragment = fragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = fragment.requireArguments()

        var source: String
            get() = fragmentArguments.getString(SOURCE) ?: ""
            set(value) = fragmentArguments.putString(SOURCE, value)

        companion object {
            private const val SOURCE = "Source"
        }
    }
}
