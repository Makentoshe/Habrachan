package com.makentoshe.habrachan.view.post

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.di.common.CacheScope
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class PostFragment : Fragment() {

    private val cache by inject<Cache<GetPostsRequest, PostsResponse>>()

    private var position: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("position", value)
        get() = arguments!!.getInt("position")

    private var page: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("page", value)
        get() = arguments!!.getInt("page")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Toothpick.openScope(CacheScope::class.java).closeOnDestroy(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PostFragmentUi().createView(requireContext())
    }

    companion object {
        fun build(page:Int, position: Int) = PostFragment().apply {
            this.page = page
            this.position = position
        }
    }
}
