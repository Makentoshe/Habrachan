package com.makentoshe.habrachan.common.ui

import android.content.Context
import android.widget.TextView
import androidx.annotation.ColorRes

class TextScoreController(private val textView: TextView) {

    fun setScore(context: Context, score: Int, @ColorRes defaultColor: Int) {
        val controller = TextColorController(textView)
        when {
            score > 0 -> controller.setPositiveColor(context)
            score < 0 -> controller.setNegativeColor(context)
            else -> controller.setColor(context, defaultColor)
        }

        val scoreText = if (score > 0) "+".plus(score) else score.toString()
        textView.text = scoreText
    }

    fun setScoreDark(context: Context, score: Int) {
        setScore(context, score, android.R.color.black)
    }

    fun setScoreLight(context: Context, score: Int) {
        setScore(context, score, android.R.color.white)
    }
}