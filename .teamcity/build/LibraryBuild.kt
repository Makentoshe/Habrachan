package build

import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle

object LibraryBuild : VcsBaseBuild("Build library", {
    steps {
        gradle {
            name = "Build full library"
            tasks = "library:build --info"
            buildFile = "build.gradle.kts"
        }
    }
})