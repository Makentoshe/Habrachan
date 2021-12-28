plugins {
    kotlin("jvm")
    kotlin("kapt")
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    api(project(":habrachan:network:content-get"))

    implementation(project(":habrachan"))
    implementation(project(":habrachan:api"))

    implementation(project(":entity"))
    implementation(project(":network"))
    implementation(project(":network:network-common:network-common-content"))
    implementation(project(":functional"))

    // inherits from
    api(project(":application:common:arena"))

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpickVersion = dependency.version.toothpick
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpickVersion")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie-lifecycle-ktp:$toothpickVersion")
    testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing-junit5:$toothpickVersion")
}