package com.maketoshe.habrachan.application.android.screen.articles.page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.network.request.SpecType

class ArticlesPageFragment : BaseFragment() {

    companion object : Analytics(LogAnalytic()) {

        fun build(spec: SpecType) = ArticlesPageFragment().apply {
            arguments.spec = spec
        }
    }

    override val arguments = Arguments(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        capture(analyticEvent("") { "${this@ArticlesPageFragment} Attach(${arguments.spec})" })
    }

    override fun internalOnCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        capture(analyticEvent("") { "${this@ArticlesPageFragment} OnViewCreated($savedInstanceState)" })
    }

    class Arguments(fragment: ArticlesPageFragment) : FragmentArguments(fragment) {

        var spec: SpecType?
            get() = fragmentArguments.get(SPEC) as SpecType
            set(value) = fragmentArguments.putSerializable(SPEC, value)

        companion object {
            private const val SPEC = "Spec"
        }
    }
}
