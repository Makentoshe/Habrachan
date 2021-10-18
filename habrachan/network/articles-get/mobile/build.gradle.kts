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
    implementation(project(":habrachan:entity:mobile"))
    implementation(project(":habrachan:api"))
    implementation(project(":habrachan:api:mobile"))
    implementation(project(":habrachan:network"))
    implementation(project(":habrachan:network:articles-get"))

    // kotlinx.serialization - Json and other stuff serializing/deserializing
    // https://github.com/Kotlin/kotlinx.serialization
    val kotlinSerializationJsonVersion = dependency.version.serializationJson
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationJsonVersion")

    // Ktor client
    val ktorHttpClientVersion = dependency.version.ktorHttpClient
    implementation("io.ktor:ktor-client-core:$ktorHttpClientVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorHttpClientVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorHttpClientVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")

    // Mockk - mocking library for testing purposes only
    // https://github.com/mockk/mockk
    val mockkVersion = dependency.version.mockk
    testImplementation("io.mockk:mockk:$mockkVersion")
}
