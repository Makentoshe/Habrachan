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
}

buildscript {
    val kotlin_version by extra("1.5.31")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")
        classpath(kotlin("gradle-plugin", version = dependency.version.kotlin))

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}