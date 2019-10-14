package com.makentoshe.habrachan.di

import android.content.Context
import com.makentoshe.habrachan.model.post.Converter
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class ApplicationScope

class ApplicationModule(context: Context): Module() {
    init {
        bind<Converter>().toInstance(Converter(context.resources))
    }
}