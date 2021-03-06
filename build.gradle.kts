plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.20"
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

buildscript {
    val kotlin_version by extra("1.5.10")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")
        classpath(kotlin("gradle-plugin", version = "1.4.20"))

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}