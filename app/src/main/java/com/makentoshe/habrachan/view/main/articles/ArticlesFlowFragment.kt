package com.makentoshe.habrachan.view.main.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.ui.main.articles.ArticlesFlowFragmentUi

class ArticlesFlowFragment : Fragment() {

    val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ArticlesFlowFragmentUi(container).createView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val magnifyView = view.findViewById<View>(R.id.articles_flow_fragment_toolbar_magnify)
//        val slidingUpPanelLayout = view.findViewById<SlidingUpPanelLayout>(R.id.articles_flow_fragment_slidingpanel)
//        slidingUpPanelLayout.isTouchEnabled = false
//        magnifyView.setOnClickListener {
//            when (slidingUpPanelLayout.panelState) {
//                SlidingUpPanelLayout.PanelState.EXPANDED -> {
//                    slidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
//                }
//                SlidingUpPanelLayout.PanelState.COLLAPSED -> {
//                    slidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
//                    closeSoftKeyboard()
//                }
//                else -> Unit
//            }
//        }
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

