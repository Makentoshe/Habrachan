package com.makentoshe.habrachan.view.post.images

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.di.post.images.PostImageFragmentPageModule
import com.makentoshe.habrachan.di.post.images.PostImageFragmentPageScope
import com.makentoshe.habrachan.viewmodel.post.images.PostImageFragmentViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy
import kotlin.random.Random

/* Single page for resource detail view such as images or svg files */
class PostImageFragmentPage : Fragment() {

    val arguments = Arguments(this)

    private val viewModel by inject<PostImageFragmentViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(requireContext()).apply {
            setBackgroundColor(Random.nextInt())
            text = "${arguments.position}\n${arguments.source}\n$viewModel"
        }
    }

    private fun injectDependencies() {
        val module = PostImageFragmentPageModule.Factory().build(this)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, PostImageFragmentPageScope::class.java, arguments.source)
        scopes.closeOnDestroy(this).installModules(module).inject(this)
        Toothpick.closeScope(scopes)
    }

    class Factory {
        fun build(position: Int, source: String): PostImageFragmentPage {
            val fragment = PostImageFragmentPage()
            fragment.arguments.source = source
            fragment.arguments.position = position
            return fragment
        }
    }

    class Arguments(fragment: Fragment) {

        init {
            fragment.arguments = Bundle()
        }

        private val fragmentArguments = fragment.requireArguments()

        var source: String
            get() = fragmentArguments.getString(SOURCE) ?: ""
            set(value) = fragmentArguments.putString(SOURCE, value)

        var position: Int
            get() = fragmentArguments.getInt(POSITION, 0)
            set(value) = fragmentArguments.putInt(POSITION, value)

        companion object {
            private const val SOURCE = "Source"
            private const val POSITION = "Position"
        }
    }
}
