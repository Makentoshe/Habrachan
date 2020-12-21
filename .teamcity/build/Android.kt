package build

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
        ./application/android/app/build/outputs/apk/debug/ => debug
    """.trimIndent()

    steps {
        installAndroidSdk()
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

    publishArtifacts = PublishMode.SUCCESSFUL
    artifactRules = """
        ./application/android/app/build/outputs/apk/release/* => release
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
        val releaseApkOutput = "app/build/outputs/apk/release/"
        val buildToolsReference = Parameters.Configuration.AndroidBuildTools29.reference

        gradle {
            name = "Assemble unsigned release apk"
            tasks = "assembleRelease --info"
            jdkHome = "%env.JDK_18_x64%"
        }

        listFilesRecursive(Parameters.Internal.CheckoutDir)

        script {
            name = "Align apk file"
            scriptContent = """
                $buildToolsReference/zipalign -v -p 4 $releaseApkOutput/app-release-unsigned.apk $releaseApkOutput/app-release-unsigned-aligned.apk
                rm $releaseApkOutput/app-release-unsigned.apk
            """.trimIndent()
        }
//        script {
//            name = "Sign release apk"
//            scriptContent = """
//
//                # Signing apk file
//                %build-tools%/apksigner sign --ks  keystore/habrachan/habrachan_keystore.jks --ks-pass pass:%keystore-password% --key-pass pass:%keystore-password% --out %release-apk-output%/app-release-signed.apk %release-apk-output%/app-release-unsigned-aligned.apk
//                rm %release-apk-output%/app-release-unsigned-aligned.apk
//
//                # Verify sign is ok
//                %build-tools%/apksigner verify %release-apk-output%/app-release-signed.apk
//            """.trimIndent()
//        }
    }
})