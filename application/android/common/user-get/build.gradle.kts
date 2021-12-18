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

    implementation(kotlin("stdlib"))

    // Inherits
    api(project(":application:common:arena:user-get"))

    implementation(project(":habrachan"))
    implementation(project(":habrachan:api"))
    implementation(project(":habrachan:entity"))
    implementation(project(":habrachan:network:user-get"))

    implementation(project(":application:android:analytics"))
    implementation(project(":application:android:database"))
    implementation(project(":application:android:common"))
    implementation(project(":application:android:di"))

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpickVersion = dependency.version.toothpick
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpickVersion")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie-lifecycle-ktp:$toothpickVersion")
    testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing-junit5:$toothpickVersion")

    // kotlinx.serialization - Json and other stuff serializing/deserializing
    // https://github.com/Kotlin/kotlinx.serialization
    val kotlinSerializationJsonVersion = dependency.version.serializationJson
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationJsonVersion")

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttpVersion = dependency.version.okhttp
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

    // Architecture components
    // https://developer.android.com/topic/libraries/architecture
    val architectureVersion = dependency.version.androidArchitecture
    implementation("android.arch.lifecycle:extensions:$architectureVersion")
    implementation("android.arch.lifecycle:viewmodel:$architectureVersion")

    // Coroutines for android ViewModel
    val coroutinesViewModelVersion = dependency.version.androidCoroutinesViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$coroutinesViewModelVersion")


    val coreVersion = dependency.version.androidCore
    implementation("androidx.core:core-ktx:$coreVersion")

    val appcompatVersion = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$appcompatVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")
    
    val androidTestCoreVersion = dependency.version.androidTestCore
    testImplementation("androidx.test:core:$androidTestCoreVersion")

    // Mockk - mocking library for testing purposes only
    // https://github.com/mockk/mockk
    val mockkVersion = dependency.version.mockk
    testImplementation("io.mockk:mockk:$mockkVersion")

    // Robolectric - unit tests with android environment
    // http://robolectric.org
    val robolectricVersion = dependency.version.androidTestRobolectric
    testImplementation("org.robolectric:robolectric:$robolectricVersion")

    // For testing with android architecture
    // https://stackoverflow.com/questions/48049131/cannot-resolve-symbol-instanttaskexecutorrule
    val testArchitectureVersion = dependency.version.androidTestArchitecture
    testImplementation("androidx.arch.core:core-testing:$testArchitectureVersion")
}
