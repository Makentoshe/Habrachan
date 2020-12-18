import jetbrains.buildServer.configs.kotlin.v2019_2.Parameter

object Parameters {
    // Configuration parameters are not passed into build, can be used in references only.
    val AndroidSdkUrl =
        Parameter("ANDROID_SDK_URL", "https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip")

    // Environment variables will be added to the environment of the processes launched by the build runner
    val AndroidHome = Parameter("env.ANDROID_HOME", "%teamcity.build.checkoutDir%/.android-sdk")

    // Configuration parameters are not passed into build, can be used in references only.
    val AndroidBuildTools29 = Parameter("build-tools", "%${AndroidHome.name}%/build-tools/29.0.3/")

    // TODO fix hardcoded value
    // Environment variables will be added to the environment of the processes launched by the build runner
    val JavaHome8A = Parameter("JAVA_HOME", "%env.JDK_1_8_x64%")
    val JavaHome8B = Parameter("env.JAVA_HOME", "%env.JDK_1_8_x64%")
}

/** Returns a parameter name wrapped with teamcity reference indicator */
val Parameter.reference: String
    get() = "%$name%"