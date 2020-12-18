package build

import Parameters
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import reference

object Android : PipelineBuildVcs("Android", {

    params {
        add(Parameters.Configuration.JavaHome8)
        add(Parameters.Environment.JavaHome8)
    }

    steps {
        script {
            name = "Test test"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = """
                echo ${'$'}JAVA_HOME
                echo %JAVA_HOME%
            """.trimIndent()
        }
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
        script {
            val androidVersion = "29"
            val androidBuildToolsVersion = "29.0.3"
            val sdkmanager = "${Parameters.Environment.AndroidHome.reference}/tools/bin/sdkmanager"
            name = "Update Android SDK 29"
            scriptContent = """
                $sdkmanager --update
                $sdkmanager "build-tools;$androidBuildToolsVersion" "platforms;android-$androidVersion" "platform-tools"
            """.trimIndent()
        }
    }
})