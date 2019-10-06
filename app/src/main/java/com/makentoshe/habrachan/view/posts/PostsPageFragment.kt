package com.makentoshe.habrachan.view.posts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.posts.PostsPageFragmentModule
import com.makentoshe.habrachan.di.posts.PostsPageFragmentScope
import com.makentoshe.habrachan.ui.posts.PostsPageFragmentUi
import com.makentoshe.habrachan.viewmodel.posts.PostsPageViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class PostsPageFragment : Fragment() {

    private val uiFactory by inject<PostsPageFragmentUi>()

    private val vmFactory by inject<PostsPageViewModel.Factory>()

    private val viewModel: PostsPageViewModel
        get() = ViewModelProviders.of(this, vmFactory)[PostsPageViewModel::class.java]

    private var position: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("Position", value)
        get() = arguments!!.getInt("Position")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val module = PostsPageFragmentModule(position)
        Toothpick.openScope(PostsPageFragmentScope::class.java).installModules(module).closeOnDestroy(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return uiFactory.createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showProgressBar()
    }

    private fun initRecyclerView(adapter: RecyclerView.Adapter<*>) {
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.main_posts_page_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun showRecyclerView() {
        requireView().findViewById<RecyclerView>(R.id.main_posts_page_recyclerview).visibility = View.VISIBLE
    }

    private fun hideRecyclerView() {
        requireView().findViewById<RecyclerView>(R.id.main_posts_page_recyclerview).visibility = View.GONE
    }

    private fun showProgressBar() {
        requireView().findViewById<View>(R.id.main_posts_page_progressbar).visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        requireView().findViewById<View>(R.id.main_posts_page_progressbar).visibility = View.GONE
    }

    companion object {
        fun build(position: Int) = PostsPageFragment().apply {
            this.position = position
        }
    }
}

