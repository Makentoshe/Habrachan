package com.makentoshe.habrachan.view.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.random.Random

class PostsPageFragment : Fragment() {

    private var position: Int
    set(value) {
        if (arguments == null) {
            arguments = Bundle()
        }
        arguments!!.putInt("position", value)
    }
    get() = arguments!!.getInt("position")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(requireContext()).apply {
            setBackgroundColor(Random.nextInt())
            text = position.toString()
        }
    }

    companion object {
        fun build(position: Int): PostsPageFragment {
            return PostsPageFragment().apply {
                this.position = position
            }
        }
    }
}