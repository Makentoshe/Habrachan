package com.makentoshe.habrachan.application.android.screen.comments.model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.paging.PagingDataAdapter
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig

class CommentAdapter(
    private val fragmentManager: FragmentManager
) : PagingDataAdapter<CommentEntityModel, CommentViewHolder>(CommentDiffUtilItemCallback()) {

    companion object {
        inline fun capture(level: Int, message: () -> String) {
            if (BuildConfig.DEBUG) return
            Log.println(level, "CommentAdapter", message())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CommentViewHolder(inflater.inflate(R.layout.layout_comment_item, parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val model = getItem(position) ?: return capture(Log.ERROR) {
            "Comment is null at position $position"
        }

        val controller = CommentViewController(holder)
        controller.render(model.comment)
        controller.setVoteListener({
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }, {
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        })
    }
}
