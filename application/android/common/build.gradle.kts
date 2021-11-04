plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlin-kapt")
    kotlin("plugin.serialization")
}

android {
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE*")
        exclude("META-INF/NOTICE*")
        exclude("META-INF/*.kotlin_module")
    }

    compileSdkVersion(dependency.build.compileSdkVersion)
    defaultConfig {
        minSdkVersion(dependency.build.minSdkVersion)
        targetSdkVersion(dependency.build.targetSdkVersion)
        versionCode = dependency.build.versionCode
        versionName = dependency.build.versionName
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(kotlin("stdlib"))
    implementation(project(":habrachan"))
    implementation(project(":habrachan:api"))


    implementation(project(":functional"))

    implementation(project(":network"))
    implementation(project(":entity"))

    implementation(project(":application:android:database"))

    // Material components
    // https://github.com/material-components/material-components-android
    val material = dependency.version.androidMaterialDesign
    implementation("com.google.android.material:material:$material")

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpick = dependency.version.toothpick
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpick")

    // kotlinx.serialization - Json and other stuff serializing/deserializing
    // https://github.com/Kotlin/kotlinx.serialization
    val kotlinSerializationJsonVersion = dependency.version.serializationJson
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationJsonVersion")

    // Android EncryptedSharedPreferences and other encryption stuff
    // https://developer.android.com/topic/security/data
    val androidSecurityCryptoVersion = dependency.version.androidSecurityCrypto
    implementation("androidx.security:security-crypto:$androidSecurityCryptoVersion")

    // Binary shared preferences (only for 21 < api < 23)
    // https://github.com/yandextaxitech/binaryprefs
    implementation("com.github.yandextaxitech:binaryprefs:1.0.1")

    val lifecycle = dependency.version.androidLifecycle
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle")

    val core = dependency.version.androidCore
    implementation("androidx.core:core-ktx:$core")

    val appcompat = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$appcompat")

    val coroutinesVersion = dependency.version.coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutinesVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")

    // Core library
    val testCoreVersion = dependency.version.androidTestCore
    debugImplementation("androidx.test:core:$testCoreVersion")
    testImplementation("androidx.test:core-ktx:$testCoreVersion")

    // Robolectric
    // http://robolectric.org
    val robolectricVersion = dependency.version.androidTestRobolectric
    testImplementation("org.robolectric:robolectric:$robolectricVersion")

    // Mockk
    // https://github.com/mockk/mockk
    val mockkVersion = dependency.version.mockk
    testImplementation("io.mockk:mockk:$mockkVersion")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
}