package com.makentoshe.habrachan.ui.comments

import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.sothree.slidinguppanel.SlidingUpPanelLayout

interface CommentsInputFragmentUi {
    val markupActionsContainer: View
    val sendButton: MaterialButton
    val sendProgressBar: ProgressBar
    val messageEditText: EditText
    val slidingPanel: SlidingUpPanelLayout
}