package com.makentoshe.habrachan.application.android.screen.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.content.model.ContentViewPagerAdapter
import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentNavigation
import kotlinx.android.synthetic.main.fragment_content.*
import toothpick.ktp.delegate.inject

class ContentFragment : CoreFragment() {

    companion object {
        fun build(source: String) = ContentFragment().apply {
            arguments.source = source
        }
    }

    override val arguments = Arguments(this)

    private val navigation by inject<ContentNavigation>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_content_viewpager.adapter = ContentViewPagerAdapter(this)
        fragment_content_viewpager.setCurrentItem(1, false)
        fragment_content_viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position != 1) {
                    fragment_content_viewpager.isUserInputEnabled = false
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (position == 0) {
                    fragment_content_viewpager.alpha = positionOffset * positionOffset
                }
                if (position == 1) {
                    fragment_content_viewpager.alpha = -(positionOffset * positionOffset) + 1
                }
                if (fragment_content_viewpager.alpha <= 0.1) {
                    navigation.back()
                }
            }
        })
    }

    class Arguments(fragment: ContentFragment) : CoreFragment.Arguments(fragment) {

        var source: String
            set(value) = fragmentArguments.putString(SOURCE, value)
            get() = fragmentArguments.getString(SOURCE)!!

        companion object {
            private const val SOURCE = "ImageSource"
        }
    }
}