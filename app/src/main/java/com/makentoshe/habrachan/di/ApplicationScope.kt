package com.makentoshe.habrachan.di

import android.content.Context
import com.makentoshe.habrachan.common.html.SpannedFactory
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.common.repository.RawResourceRepository
import com.makentoshe.habrachan.di.common.RepositoryScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class ApplicationScope

class ApplicationModule(context: Context) : Module() {

    private val inputStreamRepository by inject<InputStreamRepository>()

    init {
        Toothpick.openScope(RepositoryScope::class.java).inject(this)
        val imageGetter = SpannedFactory.ImageGetter(inputStreamRepository, context.resources)
        bind<SpannedFactory>().toInstance(SpannedFactory(imageGetter))
    }
}