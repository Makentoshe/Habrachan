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
    implementation(project(":entity:entity-native"))
    implementation(project(":functional"))

    // Network module for access abstracts
    implementation(project(":network"))
    implementation(project(":network:network-native"))
    implementation(project(":network:network-native:network-native-common"))
    implementation(project(":network:network-native:network-native-comments-get"))

    // Module for testing purposes
    testImplementation(project(":network:network-native:network-native-test"))

    // Module to test
    implementation(project(":network:network-native:network-native-comments-get"))

    // Gson
    // https://github.com/google/gson
    val gsonVersion = dependency.version.gson
    implementation("com.google.code.gson:gson:$gsonVersion")

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttpVersion = dependency.version.okhttp
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

    // Retrofit
    // https://github.com/square/retrofit
    val retrofitVersion = dependency.version.retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Kotlin coroutines
    // https://github.com/Kotlin/kotlinx.coroutines
    val coroutinesVersion = dependency.version.coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")
}

// idkw, but "onlyIf" does not disables/skips this task, so this is a workaround
tasks.test.configure {
    // for ci/cd: this should be managed by build system.
    // we should disable tests by default (ide), because the REAL api will be invoked on each build
    // and may cause accident DDoS
    enabled = project.hasProperty("allow-network-test")
}
