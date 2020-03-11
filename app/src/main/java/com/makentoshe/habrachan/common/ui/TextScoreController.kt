package com.makentoshe.habrachan.common.ui

import android.content.Context
import android.widget.TextView

class TextScoreController(private val textView: TextView) {

    fun setScore(context: Context, score: Int) {
        TextColorController(textView).setColorBy(context, score)
        val scoreText = if (score > 0) "+".plus(score) else score.toString()
        textView.text = scoreText
        println("sas")
    }
}