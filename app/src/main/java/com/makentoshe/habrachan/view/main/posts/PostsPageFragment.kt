package com.makentoshe.habrachan.view.main.posts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.posts.Data
import com.makentoshe.habrachan.di.AppActivityScope
import com.makentoshe.habrachan.di.main.posts.PostsFragmentScope
import com.makentoshe.habrachan.model.main.posts.PostsBroadcastReceiver
import com.makentoshe.habrachan.model.main.posts.PostsPageRecyclerViewAdapter
import com.makentoshe.habrachan.model.post.PostScreen
import com.makentoshe.habrachan.ui.main.posts.PostsPageFragmentUi
import com.makentoshe.habrachan.viewmodel.main.posts.PostsPageViewModel
import com.makentoshe.habrachan.viewmodel.main.posts.PostsPageViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class PostsPageFragment : Fragment() {

    private val router by inject<Router>()

    private val viewModelFactory by inject<PostsPageViewModelFactory>()

    private val viewModel: PostsPageViewModel
        get() = viewModelFactory.build(this, position)

    private var position: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("Position", value)
        get() = arguments!!.getInt("Position")

    private val disposables = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Toothpick.openScope(AppActivityScope::class.java).openSubScope(PostsFragmentScope::class.java).inject(this)
    }

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
            showRecyclerView()
            hideProgressBar()
        }.let(disposables::add)
        requireView().findViewById<SwipeRefreshLayout>(R.id.main_posts_page_swiperefresh).also {
            it.setOnRefreshListener { onRefresh(it) }
        }
    }

    private fun initRecyclerView(adapter: PostsPageRecyclerViewAdapter) {
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.main_posts_page_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.clickObservable.subscribe(::onElementClick).let(disposables::add)
    }

    private fun onElementClick(position: Int) {
        val screen = PostScreen(this.position, position)
        router.navigateTo(screen)
    }

    private fun onRefresh(view: SwipeRefreshLayout) {
        view.isRefreshing = false
        PostsBroadcastReceiver.sendRefreshBroadcast(requireContext())
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
