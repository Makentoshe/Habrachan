package com.makentoshe.habrachan.application.android.screen.content.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.content.ContentFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ContentFragmentInjector : FragmentInjector<ContentFragment>(
    fragmentClass = ContentFragment::class,
    action = {
        val scope = Toothpick.openScopes(ApplicationScope::class, ContentScope::class)
        scope.installModules(CommonContentModule(this)).closeOnDestroy(fragment).inject(fragment)
    }
)