package com.makentoshe.habrachan.view.images

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
import com.makentoshe.habrachan.navigation.images.OverlayImageFragmentNavigation
import com.makentoshe.habrachan.ui.images.OverlayImageFragmentUi
import com.makentoshe.habrachan.viewmodel.images.OverlayImageFragmentViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import pl.droidsonroids.gif.GifImageView
import toothpick.ktp.delegate.inject

class OverlayImageFragment : Fragment() {

    val arguments = OverlayImageFragmentArguments(this)

    private val viewModel by inject<OverlayImageFragmentViewModel>()
    private val disposables by inject<CompositeDisposable>()
    private val navigator by inject<OverlayImageFragmentNavigation>()

    private lateinit var panelView: SlidingUpPanelLayout
    private lateinit var messageView: TextView
    private lateinit var retryButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var gifView: GifImageView
    private lateinit var imageView: SubsamplingScaleImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return OverlayImageFragmentUi(container).inflateView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        panelView = view.findViewById(R.id.post_image_fragment_panel)
        messageView = view.findViewById(R.id.post_image_fragment_textview)
        retryButton = view.findViewById(R.id.post_image_fragment_retrybutton)
        progressBar = view.findViewById(R.id.post_image_fragment_progressbar)
        gifView = view.findViewById(R.id.post_image_fragment_gifview)
        imageView = view.findViewById(R.id.post_image_fragment_imageview)
        imageView.maxScale = 10f

        retryButton.setOnClickListener {
            viewModel.startLoad()
        }

        viewModel.bitmapObserver.observeOn(AndroidSchedulers.mainThread()).subscribe {
            imageView.setImage(ImageSource.bitmap(it))
            imageView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            retryButton.visibility = View.GONE
        }.let(disposables::add)

        viewModel.errorObserver.observeOn(AndroidSchedulers.mainThread()).subscribe {
            progressBar.visibility = View.GONE
            messageView.text = it.toString()
            messageView.visibility = View.VISIBLE
            retryButton.visibility = View.VISIBLE
        }.let(disposables::add)

        viewModel.progressObserver.observeOn(AndroidSchedulers.mainThread()).subscribe {
            messageView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            retryButton.visibility = View.GONE
        }.let(disposables::add)

        viewModel.gifDrawableObserver.observeOn(AndroidSchedulers.mainThread()).subscribe {
            gifView.setImageDrawable(it)
            gifView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            retryButton.visibility = View.GONE
            it.start()
        }.let(disposables::add)

        imageView.setOnStateChangedListener(object : SubsamplingScaleImageView.DefaultOnStateChangedListener() {
            override fun onScaleChanged(newScale: Float, origin: Int) {
                panelView.isEnabled = imageView.minScale == newScale
            }
        })

        panelView.addPanelSlideListener(object : SlidingUpPanelLayout.SimplePanelSlideListener() {
            var lock = false
            override fun onPanelSlide(panel: View, slideOffset: Float) {
                panelView.alpha = 1 - (1 - slideOffset) * 2
                if (slideOffset <= 0.2 && !lock) {
                    lock = true
                    navigator.back()
                }
            }
        })
    }

    class Factory {
        fun build(source: String) = OverlayImageFragment().apply {
            arguments.source = source
        }
    }
}
