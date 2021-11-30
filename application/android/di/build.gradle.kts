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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(kotlin("stdlib"))

    implementation(project(":application:android:analytics"))

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttpVersion = dependency.version.okhttp
    api("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.1")

    // Ktor client
    val ktorHttpClientVersion = dependency.version.ktorHttpClient
    api("io.ktor:ktor-client-core:$ktorHttpClientVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorHttpClientVersion")

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpick = "3.1.0"
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpick")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpick")
    implementation("com.github.stephanenicolas.toothpick:smoothie:$toothpick")
    implementation("com.github.stephanenicolas.toothpick:smoothie-lifecycle-ktp:$toothpick")
    testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing-junit5:$toothpick")

    val androidxCoreVersion = dependency.version.androidCore
    implementation("androidx.core:core-ktx:$androidxCoreVersion")

    val androidxAppcompatVersion = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$androidxAppcompatVersion")
}