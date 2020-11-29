plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
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
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(kotlin("stdlib"))

    // Cicerone
    // https://github.com/terrakok/Cicerone
    val cicerone = "5.1.1"
    implementation("ru.terrakok.cicerone:cicerone:$cicerone")

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpick = "3.1.0"
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpick")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpick")
    implementation("com.github.stephanenicolas.toothpick:smoothie:$toothpick")
    implementation("com.github.stephanenicolas.toothpick:smoothie-lifecycle-ktp:$toothpick")

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttp = "4.1.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttp")

    // Room
    val room = "1.1.1"
    implementation("android.arch.persistence.room:runtime:$room")
    kapt("android.arch.persistence.room:compiler:$room")

    val core = properties["version.androidx.core"]
    implementation("androidx.core:core-ktx:$core")

    val appcompat = properties["version.androidx.appcompat"]
    implementation("androidx.appcompat:appcompat:$appcompat")

    val constraint = properties["version.androidx.constraint"]
    implementation("androidx.constraintlayout:constraintlayout:$constraint")

    testImplementation("junit:junit:4.12")

    val runner = properties["version.androidx.test.runner"]
    androidTestImplementation("androidx.test:runner:$runner")

    val espresso = properties["version.androidx.test.espresso"]
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso")

}