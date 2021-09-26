package com.makentoshe.habrachan.application.android.di

import com.makentoshe.habrachan.application.android.screen.articles.navigation.*
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.*
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
        bind<ContentScreenNavigator>().toClass<ContentScreenNavigatorImpl>()
        bind<ThreadCommentsScreenNavigator>().toClass<ThreadCommentsScreenNavigatorImpl>()
        bind<DispatchCommentsScreenNavigator>().toClass<DispatchCommentsScreenNavigatorImpl>()
        bind<UserScreenNavigator>().toClass<UserScreenNavigatorImpl>()
        bind<MeScreenNavigator>().toClass<MeScreenNavigatorImpl>()
        bind<LoginScreenNavigator>().toClass<LoginScreenNavigatorImpl>()
    }
}