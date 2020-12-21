import build.AndroidBuild
import build.AndroidRelease
import build.ApplicationCore
import build.LibraryBuild
import jetbrains.buildServer.configs.kotlin.v2019_2.project
import jetbrains.buildServer.configs.kotlin.v2019_2.version

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.1"

project {

//    vcsRoot(Keystores)
    vcsRoot(GithubVcsRoot)

    buildType(LibraryBuild)
    buildType(ApplicationCore)
    buildType(AndroidBuild)
    buildType(AndroidRelease)

    params {
        add(Parameters.Configuration.AndroidSdkUrl)
        add(Parameters.Environment.AndroidHome)
        add(Parameters.Configuration.AndroidBuildTools29)
    }
}

//object Build : BuildType({
//    name = "Build"
//
//    allowExternalStatus = true
//    artifactRules = "app/build/outputs/apk/release/* => apk"
//    publishArtifacts = PublishMode.SUCCESSFUL
//
//    params {
//        param("keystore-password", "1243568790")
//    }
//
//    vcs {
//        root(DslContext.settingsRoot)
//        root(Keystores, "+:.=> keystore")
//    }
//
//    steps {
//        script {
//            name = "Clean before install"
//            executionMode = BuildStep.ExecutionMode.ALWAYS
//            scriptContent = "rm -r -f %env.ANDROID_HOME%"
//        }
//        script {
//            name = "Install Android SDK tools"
//            executionMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
//            scriptContent = """
//                ANDROID_SDK_URL=%ANDROID_SDK_URL%
//                ANDROID_HOME=%env.ANDROID_HOME%
//
//                mkdir "${'$'}ANDROID_HOME"
//                cd "${'$'}ANDROID_HOME"
//                curl -o sdk.zip "${'$'}ANDROID_SDK_URL"
//                unzip sdk.zip
//            """.trimIndent()
//        }
//        script {
//            name = "Accept licences for Android SDK"
//            scriptContent = """
//                mkdir -p "${'$'}ANDROID_HOME/licenses"
//                echo "24333f8a63b6825ea9c5514f83c2829b004d1fee" > "${'$'}ANDROID_HOME/licenses/android-sdk-license"
//            """.trimIndent()
//        }
//        script {
//            name = "Update Android SDK 29"
//            scriptContent = """
//                ANDROID_VERSION=29
//                ANDROID_BUILD_TOOLS_VERSION=29.0.3
//                ${'$'}{ANDROID_HOME}/tools/bin/sdkmanager --update
//                ${'$'}{ANDROID_HOME}/tools/bin/sdkmanager "build-tools;${'$'}{ANDROID_BUILD_TOOLS_VERSION}" "platforms;android-${'$'}{ANDROID_VERSION}" "platform-tools"
//            """.trimIndent()
//        }
//        script {
//            name = "Update Android SDK 28"
//            scriptContent = """
//                ANDROID_VERSION=28
//                ANDROID_BUILD_TOOLS_VERSION=28.0.3
//                ${'$'}{ANDROID_HOME}/tools/bin/sdkmanager --update
//                ${'$'}{ANDROID_HOME}/tools/bin/sdkmanager "build-tools;${'$'}{ANDROID_BUILD_TOOLS_VERSION}" "platforms;android-${'$'}{ANDROID_VERSION}" "platform-tools"
//            """.trimIndent()
//        }
//        gradle {
//            name = "Clean build with unit tests"
//            tasks = "clean build --info --debug"
//            jdkHome = "%env.JDK_1_8_x64%"
//            jvmArgs = "-ea -Djavax.net.ssl.trustStoreType=JKS -noverify"
//            coverageEngine = idea {
//                includeClasses = "com.makentoshe.habrachan.*"
//            }
//        }
//        gradle {
//            name = "Assemble unsigned release apk"
//            tasks = "assembleRelease"
//            jdkHome = "%env.JDK_18_x64%"
//        }
//        script {
//            name = "Sign release apk"
//            scriptContent = """
//                # Aligning apk file
//                %build-tools%/zipalign -v -p 4 %release-apk-output%/app-release-unsigned.apk %release-apk-output%/app-release-unsigned-aligned.apk
//                rm %release-apk-output%/app-release-unsigned.apk
//
//                # Signing apk file
//                %build-tools%/apksigner sign --ks  keystore/habrachan/habrachan_keystore.jks --ks-pass pass:%keystore-password% --key-pass pass:%keystore-password% --out %release-apk-output%/app-release-signed.apk %release-apk-output%/app-release-unsigned-aligned.apk
//                rm %release-apk-output%/app-release-unsigned-aligned.apk
//
//                # Verify sign is ok
//                %build-tools%/apksigner verify %release-apk-output%/app-release-signed.apk
//            """.trimIndent()
//        }
//    }
//
//    triggers {
//        vcs {
//            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_DEFAULT
//        }
//    }
//
//    features {
//        pullRequests {
//            provider = github {
//                authType = token {
//                    token = "credentialsJSON:2303ca51-869d-476c-b9d2-d3998fe18b16"
//                }
//                filterTargetBranch = "+:refs/heads/master"
//                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
//            }
//        }
//        commitStatusPublisher {
//            publisher = github {
//                githubUrl = "https://api.github.com"
//                authType = personalToken {
//                    token = "credentialsJSON:2303ca51-869d-476c-b9d2-d3998fe18b16"
//                }
//            }
//            param("github_oauth_user", "Makentoshe")
//        }
//        freeDiskSpace {
//            requiredSpace = "20gb"
//            failBuild = false
//        }
//    }
//})
