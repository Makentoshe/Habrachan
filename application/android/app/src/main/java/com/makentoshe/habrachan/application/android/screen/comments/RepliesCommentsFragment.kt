package com.makentoshe.habrachan.application.android.screen.comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig
import kotlinx.android.synthetic.main.fragment_comments_replies.*

class RepliesCommentsFragment : BottomSheetDialogFragment() {

    companion object {
        fun capture(level: Int, message: () -> String) {
            if (!BuildConfig.DEBUG) return
            Log.println(level, "RepliesCommentsFragment", message())
        }

        fun build() = RepliesCommentsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_comments_replies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val titleAdapter = CommentAdapter(childFragmentManager, 1)
        val separatorAdapter = SeparatorAdapter()
        val repliesAdapter = CommentAdapter(childFragmentManager, 3)

        fragment_comments_replies_recycler.adapter =
            ConcatAdapter(titleAdapter, separatorAdapter, repliesAdapter)

        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.drawable_divider)
        dividerItemDecoration.setDrawable(dividerDrawable!!)
        fragment_comments_replies_recycler.addItemDecoration(dividerItemDecoration)
    }
}

class SeparatorAdapter : RecyclerView.Adapter<SeparatorAdapter.SeparatorViewHolder>() {

    override fun getItemCount() = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeparatorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_comments_replies_separator, parent, false)
        return SeparatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeparatorViewHolder, position: Int) {

    }

    class SeparatorViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
