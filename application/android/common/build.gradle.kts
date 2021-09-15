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
    implementation(project(":functional"))

    implementation(project(":network"))
    implementation(project(":entity"))

    implementation(project(":application:android:database"))

    // Material components
    // https://github.com/material-components/material-components-android
    val material = dependency.version.androidMaterialDesign
    implementation("com.google.android.material:material:$material")

    val core = dependency.version.androidCore
    implementation("androidx.core:core-ktx:$core")

    val appcompat = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$appcompat")

    val coroutinesVersion = dependency.version.coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutinesVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")
}