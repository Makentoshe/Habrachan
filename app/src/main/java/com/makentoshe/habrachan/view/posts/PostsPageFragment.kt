package com.makentoshe.habrachan.view.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.model.posts.PostsPageRecyclerViewAdapter
import com.makentoshe.habrachan.ui.posts.PostsPageFragmentUi
import com.makentoshe.habrachan.viewmodel.posts.PostsPageViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class PostsPageFragment : Fragment() {

    private val viewModel: PostsPageViewModel
        get() {
            val factory = PostsPageViewModel.Factory(position)
            return ViewModelProviders.of(this, factory)[PostsPageViewModel::class.java]
        }

    private var position: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("Position", value)
        get() = arguments!!.getInt("Position")

    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PostsPageFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showProgressBar()
        viewModel.errorObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            hideProgressBar()
            hideRecyclerView()
        }.let(disposables::add)
        viewModel.postsObservable.map {
            PostsPageRecyclerViewAdapter(it)
        }.observeOn(AndroidSchedulers.mainThread()).subscribe {
            initRecyclerView(it)
            hideProgressBar()
        }.let(disposables::add)
    }

    private fun initRecyclerView(adapter: RecyclerView.Adapter<*>) {
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.main_posts_page_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        showRecyclerView()
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

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    companion object {
        fun build(position: Int) = PostsPageFragment().apply {
            this.position = position
        }
    }
}

