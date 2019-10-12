package com.makentoshe.habrachan.view.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.ui.post.PostFragmentUi

class PostFragment : Fragment() {

    private var position: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("position", value)
        get() = arguments!!.getInt("position")

    private var page: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("page", value)
        get() = arguments!!.getInt("page")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PostFragmentUi().createView(requireContext())
    }

    companion object {
        fun build(page:Int, position: Int) = PostFragment().apply {
            this.page = page
            this.position = position
        }
    }
}
