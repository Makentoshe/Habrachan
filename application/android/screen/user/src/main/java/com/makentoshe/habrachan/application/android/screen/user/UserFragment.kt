package com.makentoshe.habrachan.application.android.screen.user

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.binding.viewBinding
import com.makentoshe.habrachan.application.android.common.fragment.BindableBaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModel
import com.makentoshe.habrachan.application.android.screen.user.databinding.FragmentUserBinding
import com.makentoshe.habrachan.entity.user.component.UserLogin
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.rightOrNull
import com.makentoshe.habrachan.functional.toOption2
import com.makentoshe.habrachan.network.me.mobile.MeUserException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class UserFragment : BindableBaseFragment() {

    override val arguments = Arguments(this)

    override val binding by viewBinding<FragmentUserBinding>()

    private val meUserViewModel by inject<MeUserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments.login.fold(::onViewCreatedMe, ::onViewCreatedUser)
    }

    private fun onViewCreatedMe() {
        lifecycleScope.launch(Dispatchers.IO) {
            meUserViewModel.model.collectLatest { response ->
                println((response.content.rightOrNull()?.sourceException as? MeUserException)?.parameters)
            }
        }
    }

    private fun onViewCreatedUser(userLogin: UserLogin) {

    }

    class Arguments(fragment: UserFragment) : FragmentArguments(fragment) {

        /** If Option is Option2.None than Me will be loaded */
        var login: Option2<UserLogin>
            get() = fragmentArguments.getString(LOGIN_KEY, null)?.let(::UserLogin).toOption2()
            set(value) = fragmentArguments.putString(LOGIN_KEY, value.getOrNull()?.string)

        companion object {
            internal const val LOGIN_KEY = "UserLoginKey"
        }
    }
}

