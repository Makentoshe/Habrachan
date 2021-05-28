package src.build

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

// BaseBuild with vcs trigger. Starts automatically on vcs event
abstract class VcsBaseBuild(name: String, init: BuildType.() -> Unit): BaseBuild(name, {

    // Triggers are used to add builds to the queue either when an event occurs (like a VCS check-in)
    // or periodically with some configurable interval
    triggers {

        // VCS Trigger will add a build to the queue when a VCS check-in is detected.
        this.vcs { }
    }

    init()
})
