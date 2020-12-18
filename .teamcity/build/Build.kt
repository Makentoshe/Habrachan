package build

import GithubVcsRoot
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

abstract class VcsBaseBuild(name: String, init: BuildType.() -> Unit): BaseBuild(name, {

    // Triggers are used to add builds to the queue either when an event occurs (like a VCS check-in)
    // or periodically with some configurable interval
    triggers {

        // VCS Trigger will add a build to the queue when a VCS check-in is detected.
        this.vcs { }
    }

    init()
})

abstract class BaseBuild(name: String, init: BuildType.() -> Unit): BuildType({
    this.name = name

    // A VCS Root is a set of settings defining how TeamCity communicates with a version control system to
    // monitor changes and get sources of a build
    vcs { root(GithubVcsRoot) }

    // All requirements that build agents should meet to run this build.
    requirements {
        matches("teamcity.agent.jvm.os.family", "Linux")
    }

    init()
})