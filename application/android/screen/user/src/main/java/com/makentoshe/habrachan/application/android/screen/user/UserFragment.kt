package com.makentoshe.habrachan.application.android.screen.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.makentoshe.application.android.common.user.get.viewmodel.GetUserViewModel
import com.makentoshe.application.android.common.user.get.viewmodel.GetUserViewModelRequest
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.binding.viewBinding
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.common.fragment.BindableBaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModel
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModelResponse
import com.makentoshe.habrachan.application.android.screen.user.databinding.FragmentUserBinding
import com.makentoshe.habrachan.application.android.screen.user.view.onFailureCaused
import com.makentoshe.habrachan.application.common.arena.FlowArenaResponse
import com.makentoshe.habrachan.application.common.arena.user.me.login
import com.makentoshe.habrachan.entity.user.component.UserLogin
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.toOption2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class UserFragment : BindableBaseFragment() {

    override val arguments = Arguments(this)

    override val binding by viewBinding<FragmentUserBinding>()

    private val meUserViewModel by inject<MeUserViewModel>()
    private val getUserViewModel by inject<GetUserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments.login.fold(::onViewCreatedMe, ::onViewCreatedUser)
    }

    private fun onViewCreatedMe() {
        lifecycleScope.launch(Dispatchers.IO) {
            meUserViewModel.model.collectLatest(::onMeUserResponse)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            getUserViewModel.model.collectLatest {
                println(it)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onViewCreatedUser(userLogin: UserLogin) = Unit

    private fun onMeUserResponse(response: FlowArenaResponse<MeUserViewModelResponse, ExceptionEntry<*>>) {
        response.content.onLeft { onMeUserSuccess(response.loading, it) }.onRight { exceptionEntry ->
            if (response.loading) return@onRight else onMeUserFailure(exceptionEntry)
        }
    }

    private fun onMeUserFailure(entry: ExceptionEntry<*>) = lifecycleScope.launch(Dispatchers.Main) {
        capture(analyticEvent(throwable = entry.throwable, title = entry.title) { entry.message })
        binding.onFailureCaused(entry)
    }

    /**
     * If loading is true - then we should display content and also progress indicator
     * that indicates that loading wasn't finished yet and the new batch of data might be received.
     * */
    private fun onMeUserSuccess(loading: Boolean, response: MeUserViewModelResponse) = lifecycleScope.launch(Dispatchers.Main) {
        println("loading=$loading, response=$response")
        Toast.makeText(requireContext(), response.me.login.value.string, Toast.LENGTH_LONG).show()

        if (loading) return@launch
        getUserViewModel.channel.send(GetUserViewModelRequest(response.me.login.value))
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

    companion object : Analytics(LogAnalytic())
}

