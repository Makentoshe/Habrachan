package com.makentoshe.habrachan.application.android.screen.comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import kotlinx.android.synthetic.main.fragment_comments_article.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticleCommentsFragment : CoreFragment() {

    companion object {
        fun capture(level: Int, message: () -> String) {
            if (!BuildConfig.DEBUG) return
            Log.println(level, "ArticleCommentsFragment", message())
        }

        fun build() = ArticleCommentsFragment()
    }

    override val arguments = Arguments(this)
    private val adapter by inject<CommentAdapter>()
    private val viewModel by inject<ArticleCommentsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_comments_article, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_comments_article_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        fragment_comments_article_toolbar.setNavigationOnClickListener {
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
        }

        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.drawable_divider)
        dividerItemDecoration.setDrawable(dividerDrawable!!)
        fragment_comments_article_recycler.addItemDecoration(dividerItemDecoration)
        fragment_comments_article_recycler.adapter = adapter

        lifecycleScope.launch {
            viewModel.comments.catch {
                println(it)
            }.collect {
                adapter.submitData(it)
            }
        }
    }

    class Arguments(fragment: ArticleCommentsFragment) : CoreFragment.Arguments(fragment) {

        var articleId: Int
            get() = fragmentArguments.getInt(ARTICLE_ID)
            set(value) = fragmentArguments.putInt(ARTICLE_ID, value)

        companion object {
            private const val ARTICLE_ID = "ArticleId"
        }
    }
}
