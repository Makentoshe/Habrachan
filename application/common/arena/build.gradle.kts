plugins {
    kotlin("jvm")
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":habrachan"))

    implementation(project(":functional"))

    val versionKotlinCoroutines = dependency.version.coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$versionKotlinCoroutines")
}