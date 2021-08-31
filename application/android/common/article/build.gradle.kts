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
    implementation(project(":entity:entity-native"))

    implementation(project(":network"))
    implementation(project(":network:network-native:network-native-common"))
    implementation(project(":network:network-native"))

    implementation(project(":application:android:analytics"))
    implementation(project(":application:android:database"))
    implementation(project(":application:android:common:common-core"))
    implementation(project(":application:android:common:common-di"))

    implementation(project(":functional"))

    // Inherits
    api(project(":application:common:arena:article"))

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpickVersion = dependency.version.toothpick
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpickVersion")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie-lifecycle-ktp:$toothpickVersion")
    testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing-junit5:$toothpickVersion")

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
