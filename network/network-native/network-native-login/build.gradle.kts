plugins {
    kotlin("jvm")
}

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
    implementation(project(":network:network-native:network-native-user-me"))

    testImplementation(project(":network:network-native:network-native-test"))

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
