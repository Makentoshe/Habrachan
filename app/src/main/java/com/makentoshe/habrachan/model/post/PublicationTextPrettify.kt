package com.makentoshe.habrachan.model.post

import javax.inject.Inject

class PublicationTextPrettify @Inject constructor() {

    private var imgDisplay: String? = null
    private var imgHeight: String? = null
    private var imgMaxWidth: String? = null

    private var textJustify: String? = null

    fun setImageDisplay(display: String) {
        this.imgDisplay = display
    }

    fun setImageHeight(height: String) {
        this.imgHeight = height
    }

    fun setImageMaxWidth(maxWidth: String) {
        this.imgMaxWidth = maxWidth
    }

    fun setTextJustify(justify: String) {
        this.textJustify = justify
    }

    private fun getStyleForImage(): CharSequence {
        val builder = StringBuilder("img").append("{")
        if (imgDisplay != null) {
            builder.append("display:").append(imgDisplay).append(";")
        }
        if (imgHeight != null) {
            builder.append("height:").append(imgHeight).append(";")
        }
        if (imgMaxWidth != null) {
            builder.append("max-width:").append(imgMaxWidth).append(";")
        }
        return builder.append("}")
    }

    private fun getStyleForText(): CharSequence {
        val builder = StringBuilder("html, body").append("{")
        if (textJustify != null) {
            builder.append("text-align:").append(textJustify).append(";")
            builder.append("text-justify:auto").append(";")
        }
        return builder.append("}")
    }

    fun prettify(text: String): String {
        val imgStyle = getStyleForImage()
        val textStyle = getStyleForText()
        val style = StringBuilder("<style>").append(imgStyle).append(textStyle).append("</style>")
        return StringBuilder(style).append(text).toString()
    }
}