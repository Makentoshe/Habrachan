package com.makentoshe.habrachan.application.android.screen.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder

class CommentDetailsDialogFragment : BottomSheetDialogFragment() {

    companion object : Analytics(LogAnalytic()) {
        fun build() = CommentDetailsDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_comment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val commentView = view.findViewById<View>(R.id.dialog_comment_details_comment)
        val commentViewHolder = CommentViewHolder(commentView)
        val commentViewController = CommentViewController(commentViewHolder)
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = BottomSheetDialog(requireContext())
//        dialog.setContentView(onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState))
//        return dialog
//    }
}