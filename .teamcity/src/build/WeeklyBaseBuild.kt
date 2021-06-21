package src.build

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.ScheduleTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule

// Build runs each Monday in 3AM
abstract class WeeklyBaseBuild(name: String, init: BuildType.() -> Unit): BaseBuild(name, {

    // Triggers are used to add builds to the queue either when an event occurs (like a VCS check-in)
    // or periodically with some configurable interval
    triggers {
        schedule {
            this.weekly {
                this.dayOfWeek = ScheduleTrigger.DAY.Monday
                this.hour = 3
                this.minute = 0
            }
        }
    }

    init()
})
