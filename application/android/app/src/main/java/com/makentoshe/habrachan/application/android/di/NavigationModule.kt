package com.makentoshe.habrachan.application.android.di

import com.makentoshe.habrachan.application.android.common.navigation.StackRouter
import com.makentoshe.habrachan.application.android.common.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.application.android.common.navigation.navigator.ContentScreenNavigator
import com.makentoshe.habrachan.application.android.common.navigation.navigator.DiscussionScreenNavigator
import com.makentoshe.habrachan.application.android.common.navigation.navigator.UserScreenNavigator
import com.makentoshe.habrachan.application.android.navigation.BackwardNavigatorImpl
import com.makentoshe.habrachan.application.android.navigation.ContentScreenNavigatorImpl
import com.makentoshe.habrachan.application.android.navigation.DiscussionScreenNavigatorImpl
import com.makentoshe.habrachan.application.android.navigation.UserScreenNavigatorImpl
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module
import toothpick.ktp.binding.bind

class NavigationModule(cicerone: Cicerone<StackRouter>) : Module() {

    init {
        bind<StackRouter>().toInstance(cicerone.router)
        bind<Router>().toInstance(cicerone.router)
        bind<NavigatorHolder>().toInstance(cicerone.navigatorHolder)

        bind<BackwardNavigator>().toClass<BackwardNavigatorImpl>()
        bind<DiscussionScreenNavigator>().toClass<DiscussionScreenNavigatorImpl>()
        bind<ContentScreenNavigator>().toClass<ContentScreenNavigatorImpl>()
        bind<UserScreenNavigator>().toClass<UserScreenNavigatorImpl>()
    }
}