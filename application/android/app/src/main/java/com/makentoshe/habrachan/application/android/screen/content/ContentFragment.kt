package com.makentoshe.habrachan.application.android.screen.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.content.model.ContentViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_content.*

class ContentFragment: CoreFragment() {

    companion object {
        fun build(source: String) = ContentFragment().apply {
            arguments.source = source
        }
    }

    override val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_content_viewpager.adapter = ContentViewPagerAdapter(this)
        fragment_content_viewpager.setCurrentItem(1, false)
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