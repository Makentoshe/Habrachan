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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(kotlin("stdlib"))
    implementation(project(":application:android:analytics"))

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpick = "3.1.0"
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpick")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpick")
    implementation("com.github.stephanenicolas.toothpick:smoothie:$toothpick")
    implementation("com.github.stephanenicolas.toothpick:smoothie-lifecycle-ktp:$toothpick")
    testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing-junit5:$toothpick")

    val androidxCoreVersion = properties["version.androidx.core"]
    implementation("androidx.core:core-ktx:$androidxCoreVersion")

    val androidxAppcompatVersion = properties["version.androidx.appcompat"]
    implementation("androidx.appcompat:appcompat:$androidxAppcompatVersion")

    testImplementation("junit:junit:4.12")

    val runner = properties["version.androidx.test.runner"]
    androidTestImplementation("androidx.test:runner:$runner")

    val espresso = properties["version.androidx.test.espresso"]
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso")
}