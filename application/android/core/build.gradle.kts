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

    implementation(kotlin("stdlib"))

    implementation(project(":library:core"))
    implementation(project(":library:network"))
    implementation(project(":application:core"))

    // Room (sql database library)
    // https://developer.android.com/topic/libraries/architecture/room
    val room = "2.2.5"
    implementation("androidx.room:room-runtime:$room")
    kapt("androidx.room:room-compiler:$room")
    implementation("androidx.room:room-ktx:$room")

    // Gson
    // https://github.com/google/gson
    val gson = "2.8.6"
    implementation("com.google.code.gson:gson:$gson")

    val core = properties["version.androidx.core"]
    implementation("androidx.core:core-ktx:$core")

    val appcompat = properties["version.androidx.appcompat"]
    implementation("androidx.appcompat:appcompat:$appcompat")

    val constraint = properties["version.androidx.constraint"]
    implementation("androidx.constraintlayout:constraintlayout:$constraint")

    // Material components
    // https://github.com/material-components/material-components-android
    val material = properties["version.androidx.material"]
    implementation("com.google.android.material:material:$material")

    testImplementation("junit:junit:4.12")

    val runner = properties["version.androidx.test.runner"]
    androidTestImplementation("androidx.test:runner:$runner")

    val espresso = properties["version.androidx.test.espresso"]
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso")

    // Resolves import javax.annotation.processing.Generated does not exist error
    // https://github.com/google/dagger/issues/1339
    compileOnly("com.github.pengrad:jdk9-deps:1.0")
}