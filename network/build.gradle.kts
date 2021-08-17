plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.makentoshe.habrachan"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Core module for access entities
    implementation(project(":entity"))

    // Functional module for access support types
    implementation(project(":functional"))

    // Gson
    // https://github.com/google/gson
    val gson = dependency.version.gson
    implementation("com.google.code.gson:gson:$gson")

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttp = dependency.version.okhttp
    implementation("com.squareup.okhttp3:okhttp:$okhttp")

    // Retrofit
    // https://github.com/square/retrofit
    val retrofit = dependency.version.retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.1")

    // Kotlin coroutines
    // https://github.com/Kotlin/kotlinx.coroutines
    val coroutines = dependency.version.coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")
}

// Allows to use kotlin.Result type as a return
val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.freeCompilerArgs = listOf("-Xallow-result-return-type")

// Allows to use kotlin.Result type as a return
val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileTestKotlin.kotlinOptions.freeCompilerArgs = listOf("-Xallow-result-return-type")
