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
    buildFeatures {
        viewBinding = true
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(kotlin("stdlib"))
    implementation(project(":functional"))
    implementation(project(":habrachan"))

    implementation(project(":entity"))
    implementation(project(":entity:entity-native"))

    implementation(project(":network"))
    implementation(project(":network:network-native:network-native-article-vote"))
    implementation(project(":network:network-common:network-common-content"))

    implementation(project(":application:core"))
    implementation(project(":application:android:database"))
    implementation(project(":application:android:di"))
    implementation(project(":application:android:analytics"))
    implementation(project(":application:android:navigation"))
    implementation(project(":application:android:common"))
    implementation(project(":application:android:common:article"))
    implementation(project(":application:android:common:article-voting"))
    implementation(project(":application:android:common:avatar"))
    implementation(project(":application:android:common:arena"))

    implementation(project(":application:core"))
    implementation(project(":application:common:arena:comments"))
    implementation(project(":application:common:arena:content"))
    implementation(project(":application:android:core"))

    implementation(project(":application:android:screen"))
    implementation(project(mapOf("path" to ":application:android:screen:comments")))

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpickVersion = dependency.version.toothpick
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpickVersion")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie-lifecycle-ktp:$toothpickVersion")
    testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing-junit5:$toothpickVersion")

    // Gson
    // https://github.com/google/gson
    val gsonVersion = dependency.version.gson
    implementation("com.google.code.gson:gson:$gsonVersion")

    // Material components
    // https://github.com/material-components/material-components-android
    val material = dependency.version.androidMaterialDesign
    implementation("com.google.android.material:material:$material")

    // Architecture components
    // https://developer.android.com/topic/libraries/architecture
    val archVersion = dependency.version.androidArchitecture
    implementation("android.arch.lifecycle:extensions:$archVersion")
    implementation("android.arch.lifecycle:viewmodel:$archVersion")

    // Coroutines for android ViewModel
    val coroutinesViewModel = dependency.version.androidCoroutinesViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$coroutinesViewModel")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$coroutinesViewModel")

    // Multiline Collapsing Toolbar
    // https://github.com/opacapp/multiline-collapsingtoolbar
    val collapsing = dependency.version.androidCollapsingToolbar
    implementation("net.opacapp:multiline-collapsingtoolbar:$collapsing")

    // Jsoup - Xml parsing
    // https://github.com/jhy/jsoup
    val jsoupVersion = dependency.version.jsoup
    implementation("org.jsoup:jsoup:$jsoupVersion")

    val coreVersion = dependency.version.androidCore
    implementation("androidx.core:core-ktx:$coreVersion")

    val appCompatVersion = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$appCompatVersion")

    val constraintVersion = dependency.version.androidConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:$constraintVersion")
    
    val androidFragmentVersion = dependency.version.androidFragment
    debugImplementation("androidx.fragment:fragment-testing:$androidFragmentVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")

    // Core library
    val testCoreVersion = dependency.version.androidTestCore
    debugImplementation("androidx.test:core:$testCoreVersion") {
        exclude("androidx.text", "core")
    }
    testImplementation("androidx.test:core-ktx:$testCoreVersion")

    // Assertions
    val extJUnitVersion = dependency.version.androidTestJunit
    testImplementation("androidx.test.ext:junit:$extJUnitVersion")
    testImplementation("androidx.test.ext:junit-ktx:$extJUnitVersion")

    // AndroidJUnitRunner
    val testRunnerVersion = dependency.version.androidTestRunner
    testImplementation("androidx.test:runner:$testRunnerVersion")

    // JUnit Rules
    val testRulesVersion = dependency.version.androidTestRules
    testImplementation("androidx.test:rules:$testRulesVersion")

    val espressoCoreVersion = dependency.version.androidTestEspresso
    testImplementation("androidx.test.espresso:espresso-core:$espressoCoreVersion")

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