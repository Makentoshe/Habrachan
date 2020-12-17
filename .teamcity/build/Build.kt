package build

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import vcs.GithubVcsRoot

object StupidBuild : PipelineBuildVcs("TestBuild", {
    steps {
        gradle {
            name = "Project build"
            tasks = "build --info"
            buildFile = "build.gradle"
        }
    }
})

abstract class PipelineBuild(name: String, init: BuildType.() -> Unit) : BuildType({
    this.name = name

    // A VCS Root is a set of settings defining how TeamCity communicates with a version control system to
    // monitor changes and get sources of a build
    vcs { root(GithubVcsRoot) }

    init()
})

abstract class PipelineBuildVcs(name: String, init: BuildType.() -> Unit) : PipelineBuild(name, {

    // Triggers are used to add builds to the queue either when an event occurs (like a VCS check-in)
    // or periodically with some configurable interval
    triggers { this.vcs {  } }

    init()
})