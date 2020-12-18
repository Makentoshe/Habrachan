package build

import Parameters
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import reference
import updateAndroidSDK

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
        updateAndroidSDK("28", "28.0.3")
        updateAndroidSDK("29", "29.0.3")
        gradle {
            name = "Android application build"
            tasks = "clean :application:android:build --info --debug"
//            jdkHome = "%env.JDK_1_8_x64%"
//            jvmArgs = "-ea -Djavax.net.ssl.trustStoreType=JKS -noverify"
//            coverageEngine = idea {
//                includeClasses = "com.makentoshe.habrachan.*"
//            }
        }
    }
})