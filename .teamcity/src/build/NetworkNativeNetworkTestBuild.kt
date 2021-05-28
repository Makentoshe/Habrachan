package src.build

import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle

object NetworkNativeNetworkTestBuild: WeeklyBaseBuild("Run network tests for network:network-native module", {
    description = """
        This build start weekly each Monday at 3AM and checks network api changes.
        If api was changed during the week, the build will be failed.
    """.trimIndent()

    // These params required for compatibility with 1.8 java
    params {
        add(Parameters.Configuration.JavaHome8)
        add(Parameters.Environment.JavaHome8)
    }

    steps {
        gradle {
            name = "Run network tests"
            tasks = "clean network:network-native:network-native-test:build -Pallow-network-test"
            buildFile = "build.gradle.kts"
        }
    }
})