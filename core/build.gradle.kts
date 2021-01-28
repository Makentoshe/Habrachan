plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.makentoshe.habrachan"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Gson
    // https://github.com/google/gson
    val gson = "2.8.6"
    implementation("com.google.code.gson:gson:$gson")

}

