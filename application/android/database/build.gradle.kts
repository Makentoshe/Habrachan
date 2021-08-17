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

    implementation(project(":entity"))
    implementation(project(":entity:entity-native"))

    implementation(project(":network"))

    // Gson
    // https://github.com/google/gson
    val gsonVersion = dependency.version.gson
    implementation("com.google.code.gson:gson:$gsonVersion")

    // Room (sql database library)
    // https://developer.android.com/topic/libraries/architecture/room
    val roomVersion = dependency.version.androidRoom
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    val androidxCoreVersion = dependency.version.androidCore
    implementation("androidx.core:core-ktx:$androidxCoreVersion")

    val androidxAppcompatVersion = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$androidxAppcompatVersion")

    testImplementation("junit:junit:${dependency.version.junit}")

    val runner = dependency.version.androidTestRunner
    androidTestImplementation("androidx.test:runner:$runner")

    val espresso = dependency.version.androidTestEspresso
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso")
}

repositories {
    mavenCentral()
}