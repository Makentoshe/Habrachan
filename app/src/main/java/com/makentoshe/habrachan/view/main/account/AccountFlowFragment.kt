package com.makentoshe.habrachan.view.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.ui.main.account.AccountFlowFragmentUi
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject

class AccountFlowFragment : Fragment() {

    private val navigator by inject<Navigator>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return AccountFlowFragmentUi().create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = view.findViewById<Toolbar>(R.id.account_toolbar)
        toolbar.setNavigationOnClickListener {
            navigator.back()
        }
    }

    class Factory {
        fun build() = AccountFlowFragment()
    }

    class Navigator(private val router: Router) {
        fun back() = router.exit()
    }
}