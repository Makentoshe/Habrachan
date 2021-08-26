plugins {
    id("org.jetbrains.kotlin.jvm") version Dependency.version.kotlin
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

buildscript {
    val kotlin_version by extra("1.5.30")
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