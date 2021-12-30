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
    implementation(project(":habrachan:entity"))
    implementation(project(":habrachan:api"))
    implementation(project(":habrachan:network"))

    // kotlinx.serialization - Json and other stuff serializing/deserializing
    // https://github.com/Kotlin/kotlinx.serialization
    val kotlinSerializationJsonVersion = dependency.version.serializationJson
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationJsonVersion")

    val ktorHttpClientVersion = dependency.version.ktorHttpClient
    implementation("io.ktor:ktor-client-core:$ktorHttpClientVersion")
}
