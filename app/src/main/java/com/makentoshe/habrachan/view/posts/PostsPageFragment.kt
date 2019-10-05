package com.makentoshe.habrachan.view.posts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.posts.PostsPageFragmentModule
import com.makentoshe.habrachan.di.posts.PostsPageFragmentScope
import com.makentoshe.habrachan.ui.posts.PostsPageFragmentUi
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class PostsPageFragment : Fragment() {

    private val uiFactory by inject<PostsPageFragmentUi>()

    private var position: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("Position", value)
        get() = arguments!!.getInt("Position")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val module = PostsPageFragmentModule()
        Toothpick.openScope(PostsPageFragmentScope::class.java).installModules(module).closeOnDestroy(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return uiFactory.createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.textview).text = position.toString()
    }

    companion object {
        fun build(position: Int) = PostsPageFragment().apply {
            this.position = position
        }
    }
}
