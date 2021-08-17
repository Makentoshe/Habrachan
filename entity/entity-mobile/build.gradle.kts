plugins {
    kotlin("jvm")
}

group = "com.makentoshe.habrachan.entity"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":entity"))
    
    // Gson
    // https://github.com/google/gson
    val gson = dependency.version.gson
    implementation("com.google.code.gson:gson:$gson")
}
