plugins {
    kotlin("jvm")
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":entity"))
    implementation(project(":network"))
    implementation(project(":functional"))

    // inherits from
    implementation(project(":application:common:arena"))
}