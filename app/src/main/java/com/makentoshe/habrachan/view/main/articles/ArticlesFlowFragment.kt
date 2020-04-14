package com.makentoshe.habrachan.view.main.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.ui.main.articles.ArticlesFlowFragmentUi
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import toothpick.ktp.delegate.inject

class ArticlesFlowFragment : Fragment() {

    val arguments = Arguments(this)
    private val sessionDao by inject<SessionDao>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ArticlesFlowFragmentUi(container).createView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val slidingUpPanelLayout = view.findViewById<SlidingUpPanelLayout>(R.id.articles_flow_fragment_slidingpanel)
        slidingUpPanelLayout.isTouchEnabled = false

        val toolbar = view.findViewById<Toolbar>(R.id.articles_flow_fragment_toolbar)
        toolbar.inflateMenu(R.menu.main_search)
        toolbar.setOnMenuItemClickListener(::onSearchMenuItemClick)

        val collapsingToolbar = view.findViewById<CollapsingToolbarLayout>(R.id.articles_flow_fragment_collapsing)
        collapsingToolbar.isTitleEnabled = false
        toolbar.title = sessionDao.get()!!.articlesRequest.toString(requireContext())
    }

    private fun onSearchMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId != R.id.action_search) return false
        val slidingUpPanelLayout = view?.findViewById<SlidingUpPanelLayout>(R.id.articles_flow_fragment_slidingpanel)
        return when (slidingUpPanelLayout?.panelState) {
            SlidingUpPanelLayout.PanelState.EXPANDED -> {
                slidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                true
            }
            SlidingUpPanelLayout.PanelState.COLLAPSED -> {
                slidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                closeSoftKeyboard()
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireFragmentManager().fragments.filterIsInstance<ArticlesFragment>().forEach {
            requireFragmentManager().beginTransaction().remove(it).commit()
        }
    }

    private fun closeSoftKeyboard() {
        val imm = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    class Factory {
        fun build(page: Int): ArticlesFlowFragment {
            val fragment = ArticlesFlowFragment()
            fragment.arguments.page = page
            return fragment
        }
    }

    class Arguments(private val articlesFlowFragment: ArticlesFlowFragment) {

        init {
            val fragment = articlesFlowFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = articlesFlowFragment.requireArguments()

        var page: Int
            get() = fragmentArguments.getInt(PAGE)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }
    }
}

