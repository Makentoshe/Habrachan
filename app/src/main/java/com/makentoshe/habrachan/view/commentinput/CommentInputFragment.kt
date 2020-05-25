package com.makentoshe.habrachan.view.commentinput

import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.ui.commentinput.CommentInputFragmentUi
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.core.spans.BlockQuoteSpan
import io.noties.markwon.core.spans.CodeSpan
import io.noties.markwon.editor.*
import io.noties.markwon.editor.handler.EmphasisEditHandler
import io.noties.markwon.editor.handler.StrongEmphasisEditHandler
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.inlineparser.BangInlineProcessor
import io.noties.markwon.inlineparser.EntityInlineProcessor
import io.noties.markwon.inlineparser.HtmlInlineProcessor
import io.noties.markwon.inlineparser.MarkwonInlineParser
import org.commonmark.parser.InlineParserFactory
import org.commonmark.parser.Parser
import java.util.concurrent.Executors

class CommentInputFragment : Fragment() {

    private lateinit var messageEditText: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CommentInputFragmentUi(container).inflateView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnClickListener { }
        messageEditText = view.findViewById(R.id.commentinput_fragment_message_edit)
        messageEditText.movementMethod = LinkMovementMethod.getInstance()

        val inlineParserFactory: InlineParserFactory =
            MarkwonInlineParser.factoryBuilder() // no inline images will be parsed
                .excludeInlineProcessor(BangInlineProcessor::class.java) // no html tags will be parsed
                .excludeInlineProcessor(HtmlInlineProcessor::class.java) // no entities will be parsed (aka `&amp;` etc)
                .excludeInlineProcessor(EntityInlineProcessor::class.java)
                .build()


        val markwon = Markwon.builder(requireContext())
            .usePlugin(StrikethroughPlugin.create())
//            .usePlugin(LinkifyPlugin.create())
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureParser(builder: Parser.Builder) {
                    // disable all commonmark-java blocks, only inlines will be parsed
//                        builder.enabledBlockTypes(Collections.emptySet());
                    builder.inlineParserFactory(inlineParserFactory)
                }
            })
            .usePlugin(SoftBreakAddsNewLinePlugin.create())
            .build()


        val editor = MarkwonEditor.builder(markwon)
            .useEditHandler(EmphasisEditHandler())
            .useEditHandler(StrongEmphasisEditHandler())
            .useEditHandler(StrikethroughEditHandler())
            .useEditHandler(CodeEditHandler())
            .useEditHandler(BlockQuoteEditHandler())
//            .useEditHandler(LinkEditHandler())
            .build()


//        editText.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));
        val listener =
            MarkwonEditorTextWatcher.withPreRender(editor, Executors.newSingleThreadExecutor(), messageEditText)
        messageEditText.addTextChangedListener(listener)
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

internal class StrikethroughEditHandler : AbstractEditHandler<StrikethroughSpan>() {
    override fun configurePersistedSpans(builder: PersistedSpans.Builder) {
        builder.persistSpan(
            StrikethroughSpan::class.java
        ) { StrikethroughSpan() }
    }

    override fun handleMarkdownSpan(
        persistedSpans: PersistedSpans,
        editable: Editable,
        input: String,
        span: StrikethroughSpan,
        spanStart: Int,
        spanTextLength: Int
    ) {
        val match = MarkwonEditorUtils.findDelimited(input, spanStart, "~~")
        if (match != null) {
            editable.setSpan(
                persistedSpans.get(StrikethroughSpan::class.java),
                match.start(),
                match.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun markdownSpanType(): Class<StrikethroughSpan> {
        return StrikethroughSpan::class.java
    }
}

internal class CodeEditHandler : EditHandler<CodeSpan> {
    private var theme: MarkwonTheme? = null
    override fun init(markwon: Markwon) {
        theme = markwon.configuration().theme()
    }

    override fun configurePersistedSpans(builder: PersistedSpans.Builder) {
        builder.persistSpan(CodeSpan::class.java) { CodeSpan(theme!!) }
    }

    override fun handleMarkdownSpan(
        persistedSpans: PersistedSpans,
        editable: Editable,
        input: String,
        span: CodeSpan,
        spanStart: Int,
        spanTextLength: Int
    ) {
        val match = MarkwonEditorUtils.findDelimited(input, spanStart, "`")
        if (match != null) {
            editable.setSpan(
                persistedSpans.get(CodeSpan::class.java),
                match.start(),
                match.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun markdownSpanType(): Class<CodeSpan> {
        return CodeSpan::class.java
    }
}

internal class BlockQuoteEditHandler : EditHandler<BlockQuoteSpan> {
    private var theme: MarkwonTheme? = null
    override fun init(markwon: Markwon) {
        theme = markwon.configuration().theme()
    }

    override fun configurePersistedSpans(builder: PersistedSpans.Builder) {
        builder.persistSpan(
            BlockQuoteSpan::class.java
        ) { BlockQuoteSpan(theme!!) }
    }

    override fun handleMarkdownSpan(
        persistedSpans: PersistedSpans,
        editable: Editable,
        input: String,
        span: BlockQuoteSpan,
        spanStart: Int,
        spanTextLength: Int
    ) {
        // todo: here we should actually find a proper ending of a block quote...
        editable.setSpan(
            persistedSpans.get(BlockQuoteSpan::class.java),
            spanStart,
            spanStart + spanTextLength,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    override fun markdownSpanType(): Class<BlockQuoteSpan> {
        return BlockQuoteSpan::class.java
    }
}
