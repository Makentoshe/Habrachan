package com.makentoshe.habrachan.view.post.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.random.Random

class PostImageFragmentPage : Fragment() {

    val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(requireContext()).apply {
            setBackgroundColor(Random.nextInt())
            text = arguments.position.toString()
        }
    }

    class Factory {
        fun build(position: Int, source: String): PostImageFragmentPage {
            val fragment = PostImageFragmentPage()
            fragment.arguments.source = source
            fragment.arguments.position = position
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

        var position: Int
            get() = fragmentArguments.getInt(POSITION, 0)
            set(value) = fragmentArguments.putInt(POSITION, value)

        companion object {
            private const val SOURCE = "Source"
            private const val POSITION = "Position"
        }
    }
}