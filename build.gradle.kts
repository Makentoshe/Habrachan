plugins {
    id("org.jetbrains.kotlin.jvm") version Dependency.version.kotlin
    kotlin("plugin.serialization") version Dependency.version.serialization
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

    // Fixes an issue with mockk
    // https://github.com/mockk/mockk/issues/281#issuecomment-483329039
    configurations.all {
        resolutionStrategy {
            force("org.objenesis:objenesis:2.6")
        }
    }
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}