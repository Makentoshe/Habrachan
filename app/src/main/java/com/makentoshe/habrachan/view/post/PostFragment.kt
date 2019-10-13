package com.makentoshe.habrachan.view.post

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.di.common.CacheScope
import com.makentoshe.habrachan.di.common.NetworkScope
import com.makentoshe.habrachan.di.main.posts.PostsFragmentScope
import com.makentoshe.habrachan.di.main.posts.PostsPageFragmentModule
import com.makentoshe.habrachan.di.main.posts.PostsPageFragmentScope
import com.makentoshe.habrachan.di.post.PostFragmentModule
import com.makentoshe.habrachan.di.post.PostFragmentScope
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class PostFragment : Fragment() {

    private val cache by inject<Cache<GetPostsRequest, PostsResponse>>()

    private val requestFactory by inject<GetPostsRequestFactory>()

    private val postFragmentUi by inject<PostFragmentUi>()

    private var position: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("position", value)
        get() = arguments!!.getInt("position")

    private var page: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("page", value)
        get() = arguments!!.getInt("page")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return postFragmentUi.createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val request = requestFactory.stored(page + 1)
//        val post = cache.get(request)!!.data[position]
//        view.findViewById<TextView>(R.id.textview).text = post.previewHtml
    }

    companion object {
        fun build(page: Int, position: Int) = PostFragment().apply {
            this.page = page
            this.position = position
        }
    }

    private fun injectDependencies() {
        val module = createPostFragmentModule()
        val scopes = Toothpick.openScopes(
            CacheScope::class.java, NetworkScope::class.java, PostFragmentScope::class.java
        )
        scopes.installModules(module)
        scopes.inject(this)
        scopes.release()
    }

    private fun createPostFragmentModule(): PostFragmentModule {
        val module = PostFragmentModule()
        val cacheScope = Toothpick.openScope(CacheScope::class.java)
        cacheScope.inject(module)
        cacheScope.release()
        return module
    }
}

