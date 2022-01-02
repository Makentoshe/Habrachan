plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlin-kapt")
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
        consumerProguardFile("consumer-rules.pro")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(project(":entity"))
    implementation(project(":habrachan:entity"))
    implementation(project(mapOf("path" to ":functional")))

    // Cicerone
    // https://github.com/terrakok/Cicerone
    val ciceroneVersion = dependency.version.cicerone
    api("ru.terrakok.cicerone:cicerone:$ciceroneVersion")

    val coreVersion = dependency.version.androidCore
    implementation("androidx.core:core-ktx:$coreVersion")

    val appcompatVersion = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
}