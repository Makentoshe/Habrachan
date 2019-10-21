package com.makentoshe.habrachan.di.common

import android.content.Context
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.common.repository.RawResourceRepository
import okhttp3.OkHttpClient
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class RepositoryScope

class RepositoryModule(context: Context, client: OkHttpClient) : Module() {

    private val rawResourceRepository = RawResourceRepository(context.resources)

    private val inputStreamRepository = InputStreamRepository(client)

    init {
        bind<RawResourceRepository>().toInstance(rawResourceRepository)
        bind<InputStreamRepository>().toInstance(inputStreamRepository)
    }
}