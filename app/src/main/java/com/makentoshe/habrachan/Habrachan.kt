package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.di.ApplicationModule
import com.makentoshe.habrachan.di.ApplicationScope
import io.realm.Realm
import ru.terrakok.cicerone.Cicerone
import toothpick.ktp.KTP

class Habrachan : Application() {

    private val cicerone = Cicerone.create()

    init {
        KTP.openScope(ApplicationScope::class.java).installModules(
            ApplicationModule(
                cicerone
            )
        )
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

}
