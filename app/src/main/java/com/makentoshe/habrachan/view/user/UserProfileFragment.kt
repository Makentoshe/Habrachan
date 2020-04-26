package com.makentoshe.habrachan.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.Badge
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.ui.user.UserProfileFragmentUi

class UserProfileFragment : Fragment() {

    private val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserProfileFragmentUi(container).create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments.user.badges.isNotEmpty()) {
            displayBadges(view)
        }
        if (arguments.user.geo?.isSpecified == true) {
            displayLocation(view)
        }
        if (arguments.user.timeRegistered.isNotBlank()) {
            displayTimeRegistered(view)
        }
    }

    private fun displayBadges(view: View) {
        val badgesGroup = view.findViewById<ChipGroup>(R.id.user_fragment_content_profile_badges_group)
        arguments.user.badges.map(::buildChipFromBadge).forEach(badgesGroup::addView)
        badgesGroup.visibility = View.VISIBLE
        val badgesTitle = view.findViewById<TextView>(R.id.user_fragment_content_profile_badges_title)
        badgesTitle.visibility = View.VISIBLE
    }

    private fun buildChipFromBadge(badge: Badge): Chip {
        val chip = layoutInflater.inflate(R.layout.user_fragment_content_profile_badge, null, false) as Chip
        chip.text = badge.title
        chip.setOnClickListener {
            Toast.makeText(requireContext(), badge.description, Toast.LENGTH_LONG).show()
        }
        return chip
    }

    private fun displayLocation(view: View) {
        val locationTitle = view.findViewById<TextView>(R.id.user_fragment_content_profile_location_title)
        locationTitle.visibility = View.VISIBLE
        val stringBuilder = StringBuilder()
        if (arguments.user.geo?.country?.isNotBlank() == true) {
            stringBuilder.append(arguments.user.geo!!.country).append(", ")
        }
        if (arguments.user.geo?.region?.isNotBlank() == true) {
            stringBuilder.append(arguments.user.geo!!.region).append(", ")
        }
        if (arguments.user.geo?.city?.isNotBlank() == true) {
            stringBuilder.append(arguments.user.geo!!.city)
        }
        val locationView = view.findViewById<TextView>(R.id.user_fragment_content_profile_location_view)
        locationView.text = stringBuilder.toString()
        locationView.visibility = View.VISIBLE
    }

    private fun displayTimeRegistered(view: View) {
        val registeredTitle = view.findViewById<TextView>(R.id.user_fragment_content_profile_registered_title)
        registeredTitle.visibility = View.VISIBLE
        val registeredView = view.findViewById<TextView>(R.id.user_fragment_content_profile_registered_view)
        registeredView.text = arguments.user.timeRegistered
        registeredView.visibility = View.VISIBLE
    }

    class Factory {

        fun build(user: User): UserProfileFragment {
            val fragment = UserProfileFragment()
            fragment.arguments.user = user
            return fragment
        }
    }

    class Arguments(private val userProfileFragment: UserProfileFragment) {

        init {
            val fragment = userProfileFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = userProfileFragment.requireArguments()

        var user: User
            get() = User.fromJson(fragmentArguments.getString(USERACCOUNT)!!)
            set(value) = fragmentArguments.putString(USERACCOUNT, value.toJson())

        companion object {
            private const val USERACCOUNT = "UserAccount"
        }

    }
}
