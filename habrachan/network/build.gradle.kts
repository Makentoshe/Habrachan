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
    implementation(project(":habrachan:api"))

    // Ktor client - http multiplatform client
    // https://github.com/ktorio/ktor
    val ktorHttpClientVersion = dependency.version.ktorHttpClient
    implementation("io.ktor:ktor-client-core:$ktorHttpClientVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")
}