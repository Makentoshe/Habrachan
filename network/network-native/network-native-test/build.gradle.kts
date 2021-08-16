plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(project(":functional"))
    // Core module for access entities
    implementation(project(":entity"))
    implementation(project(":entity:entity-native"))
    // Network module for access abstracts
    implementation(project(":network"))
    implementation(project(":network:network-native"))
    implementation(project(":network:network-native:network-native-common"))

    // Gson
    // https://github.com/google/gson
    val gsonVersion = properties["version.gson"]
    implementation("com.google.code.gson:gson:$gsonVersion")

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttpVersion = properties["version.okhttp"]
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

    // Retrofit
    // https://github.com/square/retrofit
    val retrofitVersion = properties["version.retrofit"]
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Kotlin coroutines
    // https://github.com/Kotlin/kotlinx.coroutines
    val coroutinesVersion = properties["version.coroutines"]
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    val junitVersion = properties["version.test.junit"]
    testImplementation("junit:junit:$junitVersion")

    // Mockk - mocking library for testing purposes only
    // https://github.com/mockk/mockk
    val mockkVersion = properties["version.test.mockk"]
    implementation("io.mockk:mockk:$mockkVersion")
}

// idkw, but "onlyIf" does not disables/skips this task, so this is a workaround
tasks.test.configure {
    // for ci/cd: this should be managed by build system.
    // we should disable tests by default (ide), because the REAL api will invoked each build
    // and may cause accident DDoS
    enabled = project.hasProperty("allow-network-test")

    this.systemProperty("Anus", "Psa")
//        systemProperty "username", findProperty("username")
//        systemProperty "password", findProperty("password")
}

// Allows to use kotlin.Result type as a return
val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.freeCompilerArgs = listOf("-Xallow-result-return-type")

// Allows to use kotlin.Result type as a return
val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileTestKotlin.kotlinOptions.freeCompilerArgs = listOf("-Xallow-result-return-type")
