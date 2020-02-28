package com.makentoshe.habrachan.di.common

import android.content.Context
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.common.repository.RawResourceRepository
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class RepositoryScope

class RepositoryModule(context: Context) : Module() {

    private val client by inject<OkHttpClient>()
    private val rawResourceRepository = RawResourceRepository(context.resources)
    private val inputStreamRepository: InputStreamRepository

    init {
        Toothpick.openScopes(NetworkScope::class.java).inject(this)

        inputStreamRepository = InputStreamRepository(client)
        bind<InputStreamRepository>().toInstance(inputStreamRepository)
        bind<RawResourceRepository>().toInstance(rawResourceRepository)
    }

}