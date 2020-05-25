package com.makentoshe.habrachan.view.commentinput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.ui.commentinput.CommentInputFragmentUi

class CommentInputFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CommentInputFragmentUi(container).inflateView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnClickListener {  }
    }

    class Factory {
        fun build(comment: Comment) = CommentInputFragment()

        fun build() = CommentInputFragment()
    }
}

class CommentInputScreen(private val comment: Comment?) : com.makentoshe.habrachan.common.navigation.Screen() {
    override val fragment: Fragment
        get() = if (comment != null) {
            CommentInputFragment.Factory().build(comment)
        } else {
            CommentInputFragment.Factory().build()
        }
}