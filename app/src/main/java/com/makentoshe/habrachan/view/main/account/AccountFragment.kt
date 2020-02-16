package com.makentoshe.habrachan.view.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.ui.main.account.AccountFragmentUi

class AccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return AccountFragmentUi().create(requireContext())
    }

    class Factory {
        fun build(): AccountFragment {
            val fragment = AccountFragment()
            return fragment
        }
    }
}