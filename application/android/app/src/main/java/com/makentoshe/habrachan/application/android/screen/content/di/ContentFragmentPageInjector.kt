package com.makentoshe.habrachan.application.android.screen.content.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.content.ContentFragmentPage
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ContentFragmentPageInjector: FragmentInjector<ContentFragmentPage>(
    fragmentClass = ContentFragmentPage::class,
    action = {
        val contentScope = ContentScope(fragment.arguments.source)
        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, ContentScope::class, contentScope)
        toothpickScope.installModules(SpecifiedContentModule(this)).closeOnDestroy(fragment).inject(fragment)
    }
)