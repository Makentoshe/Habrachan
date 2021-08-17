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

    val kotlinVersion = dependency.version.kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    val slf4jVersion = dependency.version.slf4j
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    val logbackVersion = dependency.version.androidLogback
    implementation("com.github.tony19:logback-android:$logbackVersion")

    val androidxCoreVersion = dependency.version.androidCore
    implementation ("androidx.core:core-ktx:$androidxCoreVersion")

    val androidxAppcompatVersion = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$androidxAppcompatVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")

    val androidxTestJunitVersion = dependency.version.androidTestJunit
    androidTestImplementation("androidx.test.ext:junit:$androidxTestJunitVersion")

    val androidxTestEspressoVersion = dependency.version.androidTestEspresso
    androidTestImplementation("androidx.test.espresso:espresso-core:$androidxTestEspressoVersion")

}