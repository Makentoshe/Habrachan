plugins {
    kotlin("jvm")
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // Core module for access entities
    implementation(project(":entity"))
    implementation(project(":network"))

    // Functional module for access support types
    implementation(project(":functional"))

    // Module for using a test utilities for a network modules
    implementation(project(":network:network-native:network-native-test"))

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttp = "4.1.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttp")

    // Retrofit
    // https://github.com/square/retrofit
    val retrofit = "2.3.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.1")

    // Kotlin coroutines
    // https://github.com/Kotlin/kotlinx.coroutines
    val coroutines = "1.3.7"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")

    // Mockk - mocking library for testing purposes only
    // https://github.com/mockk/mockk
    val mockkVersion = dependency.version.mockk
    testImplementation("io.mockk:mockk:$mockkVersion")
}