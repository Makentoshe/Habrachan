package com.makentoshe.habrachan.application.android.screen.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.content.model.ContentActionBroadcastReceiver
import com.makentoshe.habrachan.application.android.screen.content.model.ContentViewPagerAdapter
import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentNavigation
import kotlinx.android.synthetic.main.fragment_content.*
import toothpick.ktp.delegate.inject
import kotlin.math.pow

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
        fragment_content_toolbar.inflateMenu(R.menu.menu_content)
        fragment_content_toolbar.setOnMenuItemClickListener(::onToolbarItemClick)
        fragment_content_toolbar.setNavigationOnClickListener {
            navigation.back()
        }

        fragment_content_viewpager.adapter = ContentViewPagerAdapter(this)
        fragment_content_viewpager.setCurrentItem(1, false)
        fragment_content_viewpager.registerOnPageChangeCallback(
            CustomPageChangeListener(fragment_content_viewpager, view, navigation)
        )
    }

    private class CustomPageChangeListener(
        private val viewpager: ViewPager2, private val view: View, private val navigation: ContentNavigation
    ) : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            if (position != 1) {
                viewpager.isUserInputEnabled = false
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            if (position == 0) { // x^2
                view.alpha = positionOffset * positionOffset
            }
            if (position == 1) { // (x - 1)^2
                view.alpha = (positionOffset - 1.0).pow(2).toFloat()
            }
            if (view.alpha <= 0.1) {
                navigation.back()
            }
        }
    }

    private fun onToolbarItemClick(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_download -> true.apply {
            ContentActionBroadcastReceiver.actionDownload(requireContext())
        }
        R.id.action_share -> true.apply {
            ContentActionBroadcastReceiver.actionShare(requireContext())
        }
        else -> false
    }

    class Arguments(fragment: ContentFragment) : CoreFragment.Arguments(fragment) {

        var source: String
            set(value) = fragmentArguments.putString(SOURCE, value)
            get() = fragmentArguments.getString(SOURCE)!!

        companion object {
            private const val SOURCE = "ContentSource"
        }
    }
}