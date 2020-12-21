import jetbrains.buildServer.configs.kotlin.v2019_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildSteps
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script


fun BuildSteps.updateAndroidSDK(
    androidVersion: String, buildToolsVersion: String, init: ScriptBuildStep.() -> Unit = {}
) = script {
    val sdkmanager = "${Parameters.Environment.AndroidHome.reference}/tools/bin/sdkmanager"
    name = "Update Android SDK $androidVersion"
    scriptContent = """
                $sdkmanager --update
                $sdkmanager "build-tools;$buildToolsVersion" "platforms;android-$androidVersion" "platform-tools"
            """.trimIndent()

    init()
}

fun BuildSteps.listFilesRecursive(path: String, init: ScriptBuildStep.() -> Unit = {}) =
    script {
        name = "List files in Path($path)"
        scriptContent = """
                ls -R $path
    """.trimIndent()

        init()
    }

/** Executes several steps for installing and configuring android sdk and build tools */
fun BuildSteps.installAndroidSdk() {
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
}
