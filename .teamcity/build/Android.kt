package build

import Parameters
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.PublishMode
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import reference
import updateAndroidSDK

object AndroidBuild : VcsBaseBuild("Android build", {
    description = """
        Any Android build should depends on it. 
        This build prepares environment and runs tests
    """.trimIndent()

    // These params required for compatibility with 1.8 java
    params {
        add(Parameters.Configuration.JavaHome8)
        add(Parameters.Environment.JavaHome8)
    }

    publishArtifacts = PublishMode.SUCCESSFUL
    artifactRules = """
        ./application/android/app/build/outputs/apk/debug/* => debug
    """.trimIndent()

    steps {
        script {
            name = "Clean Android home directory"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = "rm -r -f ${Parameters.Environment.AndroidHome.reference}"
        }
        script {
            name = "Install Android SDK tools"
            executionMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            scriptContent = """
                mkdir "${Parameters.Environment.AndroidHome.reference}"
                cd "${Parameters.Environment.AndroidHome.reference}"
                curl -o sdk.zip "${Parameters.Configuration.AndroidSdkUrl.reference}"
                unzip sdk.zip
            """.trimIndent()
        }
        script {
            name = "Accept licences for Android SDK"
            scriptContent = """
                mkdir -p "${Parameters.Environment.AndroidHome.reference}/licenses"
                echo "24333f8a63b6825ea9c5514f83c2829b004d1fee" > "${Parameters.Environment.AndroidHome.reference}/licenses/android-sdk-license"
            """.trimIndent()
        }
        updateAndroidSDK("28", "28.0.3")
        updateAndroidSDK("29", "29.0.3")
        gradle {
            name = "Android application build"
            tasks = "clean :application:android:build --info"
        }
    }
})


object AndroidRelease : BaseBuild("Android release", {
    description = """
        Assemble release apk, sign in and release it to the google play market.
        This build should be invoked manually.
    """.trimIndent()

    // These params required for compatibility with 1.8 java
    params {
        add(Parameters.Configuration.JavaHome8)
        add(Parameters.Environment.JavaHome8)
    }

    // Declares dependencies between builds
    dependencies {
        // Build should start after "Android"
        snapshot(AndroidBuild) {}
    }

    steps {
        script {
            name = "Test test"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = "echo sas asa anus psa"
        }
    }
})