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
    implementation(project(":network"))
    implementation(project(":network:network-common:network-common-content"))

    implementation(project(":application:common:arena:comments"))
    implementation(project(":application:common:arena:content"))
    implementation(project(":application:core"))
    implementation(project(":application:android:database"))
    implementation(project(":application:android:analytics"))
    implementation(project(":application:android:common:di"))

    implementation(project(":functional"))

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpickVersion = dependency.version.toothpick
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpickVersion")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie-lifecycle-ktp:$toothpickVersion")
    testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing-junit5:$toothpickVersion")

    // Room
    val roomVersion = dependency.version.androidRoom
    implementation("android.arch.persistence.room:runtime:$roomVersion")
    kapt("android.arch.persistence.room:compiler:$roomVersion")

    val coreVersion = dependency.version.androidCore
    implementation("androidx.core:core-ktx:$coreVersion")

    val appcompatVersion = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
}