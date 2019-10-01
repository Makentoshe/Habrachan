package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.model.ApplicationScope
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

class Habrachan : Application() {

    private val cicerone = Cicerone.create()

    private val scope = KTP.openScope(ApplicationScope::class.java).installModules(module {
        bind<Router>().toInstance(cicerone.router)
        bind<NavigatorHolder>().toInstance(cicerone.navigatorHolder)
    })

}
