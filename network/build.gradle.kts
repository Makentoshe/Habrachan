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
    val gson = "2.8.6"
    implementation("com.google.code.gson:gson:$gson")

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

    testImplementation("junit:junit:4.12")
}

// Allows to use kotlin.Result type as a return
val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.freeCompilerArgs = listOf("-Xallow-result-return-type")

// Allows to use kotlin.Result type as a return
val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileTestKotlin.kotlinOptions.freeCompilerArgs = listOf("-Xallow-result-return-type")
