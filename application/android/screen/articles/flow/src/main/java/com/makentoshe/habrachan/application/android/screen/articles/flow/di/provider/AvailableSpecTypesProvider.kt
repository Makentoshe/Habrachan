package com.makentoshe.habrachan.application.android.screen.articles.flow.di.provider

import com.makentoshe.habrachan.application.android.screen.articles.flow.model.AvailableSpecTypes
import javax.inject.Inject
import javax.inject.Provider

class AvailableSpecTypesProvider @Inject constructor() : Provider<AvailableSpecTypes> {
    override fun get(): AvailableSpecTypes = AvailableSpecTypes.default()
}