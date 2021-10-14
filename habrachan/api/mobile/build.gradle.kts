plugins {
    kotlin("jvm")
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

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")
}
