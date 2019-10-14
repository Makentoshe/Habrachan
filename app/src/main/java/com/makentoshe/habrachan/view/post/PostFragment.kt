package com.makentoshe.habrachan.view.post

import android.content.Context
import android.os.Bundle
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.common.CacheScope
import com.makentoshe.habrachan.di.common.NetworkScope
import com.makentoshe.habrachan.di.post.PostFragmentModule
import com.makentoshe.habrachan.di.post.PostFragmentScope
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class PostFragment : Fragment() {

    private val postFragmentUi by inject<PostFragmentUi>()

    private val viewModel by inject<PostFragmentViewModel>()

    private var position: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("position", value)
        get() = arguments!!.getInt("position")

    private var page: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("page", value)
        get() = arguments!!.getInt("page")

    private val disposables = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return postFragmentUi.createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.publicationObservable.subscribe {
            view.findViewById<TextView>(R.id.textview).text = it
        }.let(disposables::add)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    companion object {
        fun build(page: Int, position: Int) = PostFragment().apply {
            this.page = page
            this.position = position
        }
    }

    private fun injectDependencies() {
        val module = PostFragmentModule.Builder(position, page).build(this)
        val scopes = Toothpick.openScopes(
            CacheScope::class.java, NetworkScope::class.java, PostFragmentScope::class.java
        )
        scopes.installModules(module)
        scopes.inject(this)
        scopes.release()
    }
}
