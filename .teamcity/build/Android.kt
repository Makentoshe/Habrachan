package build

import MetadataVcsRoot
import Parameters
import installAndroidSdk
import jetbrains.buildServer.configs.kotlin.v2019_2.PublishMode
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import listFilesRecursive
import reference

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
        application/android/app/build/outputs/apk/debug/ => debug
    """.trimIndent()

    steps {
        installAndroidSdk()
        gradle {
            name = "Android application build"
            tasks = "clean :application:android:app:build"
        }
    }
})


object AndroidRelease : BaseBuild("Android release", {
    val releaseApkOutput = "application/android/app/build/outputs/apk/release/"

    description = """
        Assemble release apk, sign in and release it to the google play market.
        This build should be invoked manually.
    """.trimIndent()

    publishArtifacts = PublishMode.SUCCESSFUL
    artifactRules = "$releaseApkOutput => release"

    // These params required for compatibility with 1.8 java
    params {
        add(Parameters.Configuration.JavaHome8)
        add(Parameters.Environment.JavaHome8)
    }

    // Snapshot dependencies are used to create build chains.
    // When being a part of build chain the build of this configuration
    // will start only when all dependencies are built.
    //
    // If necessary, the dependencies will be triggered automatically.
    // Build configurations linked by a snapshot dependency can optionally
    // use revisions synchronization to ensure the same snapshot of the sources.
    dependencies {
        // Build should start after "Android"
        snapshot(AndroidBuild) {}
    }

    // A VCS Root is a set of settings defining how TeamCity communicates with a version control system to
    // monitor changes and get sources of a build
    vcs { root(MetadataVcsRoot) }

    steps {
        val buildToolsReference = Parameters.Configuration.AndroidBuildTools29.reference
        installAndroidSdk()
        gradle {
            name = "Assemble unsigned release apk"
            tasks = "clean application:android:app:assembleRelease"
            jdkHome = "%env.JDK_18_x64%"
        }
        script {
            name = "Align apk file"
            scriptContent = """
                $buildToolsReference/zipalign -v -p 4 $releaseApkOutput/app-release-unsigned.apk $releaseApkOutput/app-release-aligned.apk
            """.trimIndent()
        }
        script {
            name = "Sign aligned apk file"
            scriptContent = """
                $buildToolsReference/apksigner sign --ks  habrachan_keystore.jks --ks-pass pass:credentialsJSON:ecec29f2-58f4-44e8-896c-50e86206ff9a --key-pass pass:credentialsJSON:ecec29f2-58f4-44e8-896c-50e86206ff9a --out $releaseApkOutput/app-release-signed.apk $releaseApkOutput/app-release-aligned.apk
            """.trimIndent()
        }
        listFilesRecursive(Parameters.Internal.CheckoutDir)
//        script {
//            name = "Sign release apk"
//            scriptContent = """
//
//
//                # Verify sign is ok
//                %build-tools%/apksigner verify %release-apk-output%/app-release-signed.apk
//            """.trimIndent()
//        }
    }
})