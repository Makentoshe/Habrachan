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

    implementation(project(":application:common:arena:comments"))
    implementation(project(":application:android:database"))
    implementation(project(":application:android:common:common-core"))

    implementation(project(":functional"))

    implementation("javax.inject:javax.inject:1")

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttp = dependency.version.okhttp
    implementation("com.squareup.okhttp3:okhttp:$okhttp")

    // Markwon
    // https://github.com/noties/Markwon
    val markwon = dependency.version.androidMarkwon
    implementation("io.noties.markwon:core:$markwon")
    implementation("io.noties.markwon:html:$markwon")
    implementation("io.noties.markwon:image:$markwon")
    implementation("io.noties.markwon:ext-tables:$markwon")

    // Material components
    // https://github.com/material-components/material-components-android
    val materialVersion = dependency.version.androidMaterialDesign
    implementation("com.google.android.material:material:$materialVersion")

    // Architecture components
    // https://developer.android.com/topic/libraries/architecture
    val arch = dependency.version.androidArchitecture
    implementation("android.arch.lifecycle:extensions:$arch")
    implementation("android.arch.lifecycle:viewmodel:$arch")

    // Pagination library
    // https://developer.android.com/topic/libraries/architecture/paging
    val paginationVersion = dependency.version.androidPaging
    implementation("androidx.paging:paging-runtime-ktx:$paginationVersion")
    implementation("androidx.paging:paging-rxjava2:$paginationVersion")

    // Coroutines for android ViewModel
    val coroutinesViewModel = dependency.version.androidCoroutinesViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$coroutinesViewModel")

    val constraintVersion = dependency.version.androidConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:$constraintVersion")

    val coreVersion = dependency.version.androidCore
    implementation("androidx.core:core-ktx:$coreVersion")

    val appcompatVersion = dependency.version.androidAppCompat
    implementation("androidx.appcompat:appcompat:$appcompatVersion")

    val junitVersion = dependency.version.junit
    testImplementation("junit:junit:$junitVersion")

    val runner = dependency.version.androidTestRunner
    androidTestImplementation("androidx.test:runner:$runner")

    val espresso = dependency.version.androidTestEspresso
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso")
}