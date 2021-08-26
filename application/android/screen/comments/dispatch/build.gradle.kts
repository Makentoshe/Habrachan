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

        // Allows to use kotlin.Result type as a return
        kotlinOptions.freeCompilerArgs = listOf("-Xallow-result-return-type")

        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":entity"))

    implementation(project(":application:android:common:common-core"))

    implementation(project(":application:android:screen"))
    implementation(project(":application:android:screen:comments"))

    // Cicerone
    // https://github.com/terrakok/Cicerone
    val ciceroneVersion = dependency.version.cicerone
    implementation("ru.terrakok.cicerone:cicerone:$ciceroneVersion")

    val coreVersion = dependency.version.androidCore
    implementation("androidx.core:core-ktx:$coreVersion")

    val appCompatVersion = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$appCompatVersion")

    val constraintLayoutVersion = dependency.version.androidConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")

    val runnerVersion = dependency.version.androidTestRunner
    androidTestImplementation("androidx.test:runner:$runnerVersion")

    val espressoVersion = dependency.version.androidTestEspresso
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
}