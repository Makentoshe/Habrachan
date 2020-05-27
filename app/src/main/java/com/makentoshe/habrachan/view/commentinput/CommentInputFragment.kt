package com.makentoshe.habrachan.view.commentinput

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButtonToggleGroup
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.ui.commentinput.CommentInputFragmentUi

class CommentInputFragment : Fragment() {

    private lateinit var messageEditText: EditText
    private lateinit var messageToggleButton: MaterialButtonToggleGroup
    private lateinit var toolbar: Toolbar

    private val markupConfig = MarkupConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CommentInputFragmentUi(container).inflateView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnClickListener { }
        messageEditText = view.findViewById(R.id.commentinput_fragment_input)
//        messageToggleButton = view.findViewById(R.id.commentinput_fragment_markup)
        toolbar = view.findViewById(R.id.commentinput_fragment_toolbar)

        toolbar.setTitle(R.string.commentinput_toolbar_comment_new)

//        messageToggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
//            onMarkupConfigChanged(checkedId, isChecked)
//        }
    }

    private fun onMarkupConfigChanged(checkedId: Int, isChecked: Boolean) = when (checkedId) {
        else -> Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
    }

    class Factory {
        fun build(comment: Comment) = CommentInputFragment()

        fun build() = CommentInputFragment()
    }
}

class CommentInputScreen(private val comment: Comment? = null) : com.makentoshe.habrachan.common.navigation.Screen() {
    override val fragment: Fragment
        get() = if (comment != null) {
            CommentInputFragment.Factory().build(comment)
        } else {
            CommentInputFragment.Factory().build()
        }
}

class MarkupMessageTextHandler(private val markupConfig: MarkupConfig) : TextWatcher {

    override fun afterTextChanged(s: Editable) {
        markupConfig.appendSpans(s)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
}

data class MarkupConfig(val bold: ArrayList<Config.Bold> = ArrayList()) {

    fun appendSpans(text: Editable) {
        val iterator = bold.iterator()
        while (iterator.hasNext()) {
            val span = iterator.next()
            if (span.start > text.length) {
                iterator.remove()
            } else {
                span.appendToText(text)
            }
        }
    }

    sealed class Config {

        abstract var enabled: Boolean

        abstract var start: Int

        abstract var end: Int

        abstract fun appendToText(editable: Editable)

        class Bold(
            override var enabled: Boolean = false,
            override var start: Int = -1,
            override var end: Int = -1
        ) : Config() {

            override fun appendToText(editable: Editable) {
                val end = if (end == -1) editable.length else end
                editable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }
        }
    }
}

