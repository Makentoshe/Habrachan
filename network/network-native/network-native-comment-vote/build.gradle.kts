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
    // Network module for access abstracts
    implementation(project(":network"))
    implementation(project(":network:network-native:network-native-common"))
    testImplementation(project(":network:network-native:network-native-test"))

    implementation(project(":functional"))

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
    testImplementation("io.mockk:mockk:$mockkVersion")
}