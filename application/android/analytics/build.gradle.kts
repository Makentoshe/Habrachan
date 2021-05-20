plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE*")
        exclude("META-INF/NOTICE*")
        exclude("META-INF/*.kotlin_module")
    }
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*,jar"))))

    val kotlinVersion = properties["version.kotlin"]
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    val androidxCoreVersion = properties["version.androidx.core"]
    implementation ("androidx.core:core-ktx:$androidxCoreVersion")

    val androidxAppcompatVersion = properties["version.androidx.appcompat"]
    implementation("androidx.appcompat:appcompat:$androidxAppcompatVersion")

    val junitVersion = properties["version.junit"]
    testImplementation("junit:junit:$junitVersion")

    val androidxTestJunitVersion = properties["version.androidx.test.junit"]
    androidTestImplementation("androidx.test.ext:junit:$androidxTestJunitVersion")

    val androidxTestEspressoVersion = properties["version.androidx.test.espresso"]
    androidTestImplementation("androidx.test.espresso:espresso-core:$androidxTestEspressoVersion")

}