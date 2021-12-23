package src.build

import jetbrains.buildServer.configs.kotlin.v2019_2.PublishMode
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import src.Parameters
import src.installAndroidSdk

object AndroidBuild : VcsBaseBuild("Android build", {
    description = """
        Default Android build. This build prepares environment and runs tests.
    """.trimIndent()

    params {
        add(Parameters.Configuration.JavaHome8)
        add(Parameters.Environment.JavaHome8)
    }

    publishArtifacts = PublishMode.SUCCESSFUL
    artifactRules = """
        application/android/app/build/outputs/apk/debug/ => debug
    """.trimIndent()

    steps {
        installAndroidSdk()
        gradle {
            name = "Android application build"
            tasks = "clean build"
            buildFile = "build.gradle.kts"
            jdkHome = Parameters.Environment.JavaHome11.value
        }
    }
})