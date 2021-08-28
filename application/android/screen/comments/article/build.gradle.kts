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

    implementation(project(":application:core"))
    implementation(project(":application:common:arena:comments"))
    implementation(project(":application:common:arena:content"))
    implementation(project(":application:android:core"))

    implementation(project(":application:android:screen"))
    implementation(project(":application:android:screen:comments"))
    
    implementation(project(":application:android:database"))
    implementation(project(":application:android:analytics"))
    implementation(project(":application:android:common:navigation"))
    implementation(project(":application:android:common:arena"))
    implementation(project(":application:android:common:avatar"))
    implementation(project(":application:android:common:common-core"))
    implementation(project(":application:android:common:common-comment"))
    implementation(project(":application:android:common:common-di"))

    implementation(project(":entity"))
    implementation(project(":entity:entity-native"))

    implementation(project(":network"))
    implementation(project(":network:network-native"))
    implementation(project(":network:network-native:network-native-common"))
    implementation(project(":network:network-native:network-native-comment-vote"))
    implementation(project(":network:network-native:network-native-comments-get"))
    implementation(project(":network:network-common:network-common-content"))

    implementation(project(":functional"))

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttpVersion = dependency.version.okhttp
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

    // Retrofit
    // https://github.com/square/retrofit
    val retrofitVersion = dependency.version.retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.1")

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpickVersion = dependency.version.toothpick
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpickVersion")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie:$toothpickVersion")
    implementation("com.github.stephanenicolas.toothpick:smoothie-lifecycle-ktp:$toothpickVersion")
    testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing-junit5:$toothpickVersion")

    // Material components
    // https://github.com/material-components/material-components-android
    val materialVersion = dependency.version.androidMaterialDesign
    implementation("com.google.android.material:material:$materialVersion")

    // Architecture components
    // https://developer.android.com/topic/libraries/architecture
    val archVersion = dependency.version.androidArchitecture
    implementation("android.arch.lifecycle:extensions:$archVersion")
    implementation("android.arch.lifecycle:viewmodel:$archVersion")

    // Coroutines for android ViewModel
    val coroutinesViewModel = dependency.version.androidCoroutinesViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$coroutinesViewModel")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$coroutinesViewModel")

    // Recycler view additional classes such as ConcatAdapter
    // https://developer.android.com/jetpack/androidx/releases/recyclerview
    val recyclerViewVersion = dependency.version.androidRecyclerView
    implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")

    // Room
    val roomVersion = dependency.version.androidRoom
    implementation("android.arch.persistence.room:runtime:$roomVersion")
    kapt("android.arch.persistence.room:compiler:$roomVersion")

    // Pagination library
    // https://developer.android.com/topic/libraries/architecture/paging
    val paginationVersion = dependency.version.androidPaging
    implementation("androidx.paging:paging-runtime-ktx:$paginationVersion")
    implementation("androidx.paging:paging-rxjava2:$paginationVersion")

    // Markwon
    // https://github.com/noties/Markwon
    val markwonVersion = dependency.version.androidMarkwon
    implementation("io.noties.markwon:core:$markwonVersion")
    implementation("io.noties.markwon:html:$markwonVersion")
    implementation("io.noties.markwon:image:$markwonVersion")
    implementation("io.noties.markwon:ext-tables:$markwonVersion")

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