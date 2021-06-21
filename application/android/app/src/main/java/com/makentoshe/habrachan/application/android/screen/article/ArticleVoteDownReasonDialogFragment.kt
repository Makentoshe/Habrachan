package com.makentoshe.habrachan.application.android.screen.article

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.network.request.ArticleVote

class ArticleVoteDownReasonDialogFragment : DialogFragment() {

    companion object : Analytics(LogAnalytic()) {
        private const val tag = "ArticleVoteDownReasonDialog"
        const val request = "ArticleVoteDownReason"
        const val key = "ArticleVoteDownReasonKey"
        private const val checkedItemKey = "ArticleVoteDownCheckedItemKey"

        fun show(fragmentManager: FragmentManager) = ArticleVoteDownReasonDialogFragment().show(fragmentManager, tag)
    }

    private var checkedItem = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val restoredCheckedItem = savedInstanceState?.getInt(checkedItemKey) ?: -1
        if (savedInstanceState == null) {
            capture(analyticEvent("create dialog"))
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.article_vote_action_negative_dialog_title)
        builder.setSingleChoiceItems(R.array.article_vote_action_negative_dialog_reasons, restoredCheckedItem) { _, which ->
            checkedItem = which
        }
        builder.setPositiveButton(R.string.article_vote_action_negative_dialog_positive) { dialog, _ ->
            if (checkedItem < 0) return@setPositiveButton
            val reason = ArticleVote.Down.Reason.values()[checkedItem]
            parentFragmentManager.setFragmentResult(request, Bundle().apply { putSerializable(key, reason) })
            capture(analyticEvent("reason=$reason"))
            dialog.dismiss()
        }
        builder.setNeutralButton(R.string.article_vote_action_negative_dialog_neutral) { dialog, _ ->
            dialog.dismiss()
        }
        builder.setOnDismissListener {
            capture(analyticEvent("dismiss dialog"))
        }
        return builder.create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(checkedItemKey, checkedItem)
    }
}