package com.makentoshe.habrachan.application.android.di

import com.makentoshe.habrachan.application.android.factory.ArticlesFactoryImpl
import com.makentoshe.habrachan.application.android.navigation.BackwardNavigatorImpl
import com.makentoshe.habrachan.application.android.navigation.ContentScreenNavigatorImpl
import com.makentoshe.habrachan.application.android.navigation.DispatchCommentsScreenNavigatorImpl
import com.makentoshe.habrachan.application.android.navigation.LoginScreenNavigatorImpl
import com.makentoshe.habrachan.application.android.navigation.MeScreenNavigatorImpl
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.navigation.ThreadCommentsScreenNavigatorImpl
import com.makentoshe.habrachan.application.android.navigation.UserScreenNavigatorImpl
import com.makentoshe.habrachan.application.android.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.application.android.navigation.navigator.ContentScreenNavigator
import com.makentoshe.habrachan.application.android.navigation.navigator.DispatchCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.navigation.navigator.LoginScreenNavigator
import com.makentoshe.habrachan.application.android.navigation.navigator.MeScreenNavigator
import com.makentoshe.habrachan.application.android.navigation.navigator.ThreadCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.navigation.navigator.UserScreenNavigator
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesFactory
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

        bind<ArticlesFactory>().toClass<ArticlesFactoryImpl>()
    }
}