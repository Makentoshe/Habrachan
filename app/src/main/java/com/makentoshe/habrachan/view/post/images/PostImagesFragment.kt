package com.makentoshe.habrachan.view.post.images

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.model.post.images.PostImageViewPagerAdapter
import com.makentoshe.habrachan.ui.post.images.PostImagesFragmentUi

class PostImagesFragment : Fragment() {

    val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PostImagesFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewpager = view.findViewById<ViewPager>(R.id.view_pager)
        viewpager.adapter = PostImageViewPagerAdapter(childFragmentManager, arguments.sources)
        viewpager.currentItem = arguments.index
    }

    class Factory {
        fun build(index: Int, sources: Array<String>): PostImagesFragment {
            val fragment = PostImagesFragment()
            val arguments = fragment.arguments
            arguments.index = index
            arguments.sources = sources
            return fragment
        }
    }

    class Arguments(fragment: Fragment) {

        init {
            fragment.arguments = Bundle()
        }

        private val fragmentArguments = fragment.requireArguments()

        var index: Int
            get() = fragmentArguments.getInt(INDEX, 0)
            set(value) = fragmentArguments.putInt(INDEX, value)

        var sources: Array<String>
            get() = fragmentArguments.getStringArray(SOURCES) ?: emptyArray()
            set(value) = fragmentArguments.putStringArray(SOURCES, value)

        companion object {
            private const val INDEX = "Index"
            private const val SOURCES = "Sources"
        }
    }
}
