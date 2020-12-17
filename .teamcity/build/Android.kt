package build

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script

object Android: PipelineBuildVcs("Android", {
    steps {
        script {
            name = "Clean Android home directory"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = "rm -r -f %env.ANDROID_HOME%"
        }
        script {
            name = "Install Android SDK tools"
            executionMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            scriptContent = """
                mkdir "%env.ANDROID_HOME%"
                cd "%env.ANDROID_HOME%"
                curl -o sdk.zip "%ANDROID_SDK_URL%"
                unzip sdk.zip
            """.trimIndent()
        }
        script {
            name = "Accept licences for Android SDK"
            scriptContent = """
                mkdir -p "%env.ANDROID_HOME%/licenses"
                echo "24333f8a63b6825ea9c5514f83c2829b004d1fee" > "%env.ANDROID_HOME%/licenses/android-sdk-license"
            """.trimIndent()
        }
    }
})