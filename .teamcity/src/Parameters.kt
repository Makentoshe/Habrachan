package src

import jetbrains.buildServer.configs.kotlin.v2019_2.Parameter
import src.Parameters.Environment.AndroidHome

object Parameters {
    /**
     * Environment variables will be added to the environment of the processes
     * launched by the build runner
     */
    object Environment {

        /** Home directory for android sdk, used in build */
        val AndroidHome = Parameter("env.ANDROID_HOME", "${Internal.CheckoutDir}.android-sdk")

        val JavaHome8 = Parameter("env.JAVA_HOME", "%env.JDK_1_8_x64%")
    }

    /** Configuration parameters are not passed into build, can be used in references only. */
    object Configuration {

        val AndroidSdkUrl = Parameter("ANDROID_SDK_URL", "https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip")

        val AndroidBuildTools29 = Parameter("build-tools", "%${AndroidHome.name}%/build-tools/29.0.3/")

        val JavaHome8 = Parameter("JAVA_HOME", "%env.JDK_1_8_x64%")
    }

    /** Internal params from TeamCity */
    object Internal {

        /** Path to repository checkout */
        const val CheckoutDir = "%teamcity.build.checkoutDir%"

    }

    /**
     * Generated token will be stored on the server and can be used
     * in project configuration files instead of the secure value.
     */
    object Credential {

        /** Token for android's keystore */
        val Keystore = Parameter("keystore", "credentialsJSON:ecec29f2-58f4-44e8-896c-50e86206ff9a")

        /** Token contains a real used token from official android app */
        val HabrClientToken = Parameter("habr-client-token", "credentialsJSON:34bcbef3-91a0-4304-8ea9-a56f66004513")

        /** Token contains a real api token from official android app */
        val HabrApiToken = Parameter("habr-api-token", "credentialsJSON:a667b9ee-ae50-438e-aa21-0dee3c39af99")

        val HabrUserToken = Parameter("habr-user-token", "credentialsJSON:c64b624b-4d32-477f-9517-2bcd9fac854b")
    }
}

/** Returns a parameter name wrapped with teamcity reference indicator */
val Parameter.reference: String
    get() = "%$name%"