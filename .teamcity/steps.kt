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

fun BuildSteps.listFilesRecursive(path: String, init: ScriptBuildStep.() -> Unit = {}) = script {
    name = "List files in Path($path)"
    scriptContent = """
                ls -R $path
    """.trimIndent()

    init()
}
