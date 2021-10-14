plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":habrachan:entity"))

    implementation(project(":habrachan"))
    implementation(project(":habrachan:api"))

    // kotlinx.serialization - Json and other stuff serializing/deserializing
    // https://github.com/Kotlin/kotlinx.serialization
    val kotlinSerializationJsonVersion = dependency.version.serializationJson
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationJsonVersion")

    // kotlinx.serialization - Json and other stuff serializing/deserializing
    // https://github.com/Kotlin/kotlinx.serialization
    val kotlinSerializationPropertiesVersion = dependency.version.serializationProperties
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-properties:$kotlinSerializationPropertiesVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")


}
