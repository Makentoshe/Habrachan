package com.makentoshe.habrachan.application.android.common.comment

import android.text.style.ClickableSpan
import android.view.View
import com.makentoshe.habrachan.application.android.common.navigation.navigator.ContentScreenNavigator
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.Prop
import org.commonmark.node.Image

class ImageClickMarkwonPlugin(
    private val navigator: ContentScreenNavigator
) : AbstractMarkwonPlugin() {
    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        val origin = builder.getFactory(Image::class.java) ?: return
        builder.setFactory(Image::class.java) { configuration, props ->
            arrayOf(origin.getSpans(configuration, props), object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val source = props.get<String>(Prop.of("image-destination")) ?: return
                    navigator.toContentScreen(source)
                }
            })
        }
    }
}