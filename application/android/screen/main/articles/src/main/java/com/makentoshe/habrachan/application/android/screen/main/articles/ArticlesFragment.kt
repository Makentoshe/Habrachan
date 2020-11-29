package com.makentoshe.habrachan.application.android.screen.main.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.FragmentArguments

class ArticlesFragment : CoreFragment() {

    companion object {
        fun build(): ArticlesFragment {
            val fragment = ArticlesFragment()
            return fragment
        }
    }

    val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(requireContext()).apply { text = "Articles" }
    }

    class Arguments(fragment: ArticlesFragment) : FragmentArguments<ArticlesFragment>(fragment) {

    }
}

