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
    api(project(":application:common:arena"))
    
    implementation("javax.inject:javax.inject:1")
}