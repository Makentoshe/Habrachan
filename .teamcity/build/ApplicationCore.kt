package build

import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle

object ApplicationCore: VcsBaseBuild("Application core", {
    steps {
        gradle {
            name = "Build application core module"
            tasks = "application:core:build --info"
            buildFile = "build.gradle"
        }
    }
})