//import java.util.*

plugins {
    id("com.android.application")
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
        applicationId = "com.makentoshe.habrachan"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 9
        versionName = "0.7.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

//        val localPropertiesFile = project.rootProject.file("local.properties")
//        if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
//            val properties = Properties().apply { load(localPropertiesFile.inputStream()) }
//        }
//        buildConfigField("String", "API_ANALYTICS", "\"${properties["api.analytics"]!!}\"")
        buildConfigField("String", "CLIENT_KEY", "\"85cab69095196f3.89453480\"")
        buildConfigField("String", "API_KEY", "\"173984950848a2d27c0cc1c76ccf3d6d3dc8255b\"")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        // Allows to use kotlin.Result type as a return
        kotlinOptions.freeCompilerArgs = listOf("-Xallow-result-return-type")
    }
    kotlinOptions {
        // for inlining bytecode for some methods
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
//    testOptions {
//        unitTests {
//            includeAndroidResources = true
//        }
//    }
}

repositories {
    google()
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*,jar"))))

    implementation(kotlin("stdlib"))

    implementation(project(":application:core"))
    implementation(project(":application:android:core"))
    implementation(project(":application:android:database"))
    implementation(project(":application:android:analytics"))
    implementation(project(":application:android:common:common-core"))
    implementation(project(":application:android:common:common-comment"))
    implementation(project(":application:android:common:common-di"))

    implementation(project(":entity"))
    implementation(project(":entity:entity-native"))
    implementation(project(":entity:entity-mobile"))

    implementation(project(":network"))
    implementation(project(":network:network-native:network-native-common"))
    implementation(project(":network:network-native:network-native-login"))
    implementation(project(":network:network-native:network-native-article-vote"))
    implementation(project(":network:network-native:network-native-article-get"))
    implementation(project(":network:network-native:network-native-articles-get"))
    implementation(project(":network:network-native:network-native-comment-vote"))
    implementation(project(":network:network-native:network-native-comments-get"))
    implementation(project(":network:network-native:network-native-user-get"))
    implementation(project(":network:network-native:network-native-user-me"))
    implementation(project(":network:network-mobile"))
    implementation(project(":network:network-mobile:network-mobile-login"))

    implementation(project(":network:network-common:network-common-content"))

    implementation(project(":functional"))

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttp = "4.1.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttp")

    // Retrofit
    // https://github.com/square/retrofit
    val retrofit = "2.3.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofit")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.1")

    // Toothpick
    // https://github.com/stephanenicolas/toothpick
    val toothpick = "3.1.0"
    implementation("com.github.stephanenicolas.toothpick:ktp:$toothpick")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpick")
    implementation("com.github.stephanenicolas.toothpick:smoothie:$toothpick")
    implementation("com.github.stephanenicolas.toothpick:smoothie-lifecycle-ktp:$toothpick")
    testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing-junit5:$toothpick")

    // Cicerone
    // https://github.com/terrakok/Cicerone
    val cicerone = "5.1.1"
    implementation("ru.terrakok.cicerone:cicerone:$cicerone")

    // Material components
    // https://github.com/material-components/material-components-android
    val material = "1.3.0-alpha01"
    implementation("com.google.android.material:material:$material")

    // Architecture components
    // https://developer.android.com/topic/libraries/architecture
    val arch = "1.1.1"
    implementation("android.arch.lifecycle:extensions:$arch")
    implementation("android.arch.lifecycle:viewmodel:$arch")

    // Coroutines for android ViewModel
    val coroutinesViewModel = "2.3.0-beta01"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$coroutinesViewModel")

    // Recycler view additional classes such as ConcatAdapter
    // https://developer.android.com/jetpack/androidx/releases/recyclerview
    val recyclerview = "1.2.0-beta01"
    implementation("androidx.recyclerview:recyclerview:$recyclerview")

    // RxJava2
    // https://github.com/ReactiveX/RxJava
    val rxjava2 = "3.0.0-RC3"
    implementation("io.reactivex.rxjava3:rxjava:$rxjava2")

    // RxAndroid
    // https://github.com/ReactiveX/RxAndroid
    val rxandroid = "2.1.1"
    implementation("io.reactivex.rxjava2:rxandroid:$rxandroid")

    // SlidingUpPanelLayout
    // https://github.com/umano/AndroidSlidingUpPanel
    val slidinguppanel = "3.4.0"
    implementation("com.sothree.slidinguppanel:library:$slidinguppanel")

    // Xml parsing
    // https://github.com/jhy/jsoup
    val jsoup = "1.11.3"
    implementation("org.jsoup:jsoup:$jsoup")

    // Dexter (request permissions)
    // https://github.com/Karumi/Dexter/
    val dexter = "6.2.2"
    implementation("com.karumi:dexter:$dexter")

    // Room
    val room = "1.1.1"
    implementation("android.arch.persistence.room:runtime:$room")
    kapt("android.arch.persistence.room:compiler:$room")

    // Epoxy
    // https://github.com/airbnb/epoxy
    val epoxy = "3.8.0"
    implementation("com.airbnb.android:epoxy:$epoxy")
    kapt("com.airbnb.android:epoxy-processor:$epoxy")
    implementation("com.airbnb.android:epoxy-paging:$epoxy")

    // Pagination library
    // https://developer.android.com/topic/libraries/architecture/paging
    val pagination = "3.0.0-alpha11"
    implementation("androidx.paging:paging-runtime-ktx:$pagination")
    implementation("androidx.paging:paging-rxjava2:$pagination")

    // Subsampling scale image view
    // https://github.com/davemorrissey/subsampling-scale-image-view
    implementation("com.davemorrissey.labs:subsampling-scale-image-view:3.10.0")

    val gif = "1.2.19"
    implementation("pl.droidsonroids.gif:android-gif-drawable:$gif")

    // Multiline Collapsing Toolbar
    // https://github.com/opacapp/multiline-collapsingtoolbar
    val collapsing = "27.1.1"
    implementation("net.opacapp:multiline-collapsingtoolbar:$collapsing")

    // Markwon
    // https://github.com/noties/Markwon
    val markwon = "4.3.1"
    implementation("io.noties.markwon:core:$markwon")
    implementation("io.noties.markwon:html:$markwon")
    implementation("io.noties.markwon:image:$markwon")
    implementation("io.noties.markwon:ext-tables:$markwon")

    val core = properties["version.androidx.core"]
    implementation("androidx.core:core-ktx:$core")

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    testImplementation("junit:junit:4.12")

    val testCore = "1.2.0"
    androidTestImplementation("androidx.test:core:$testCore")
    androidTestImplementation("androidx.test:core-ktx:$testCore")

    val extJUnit = "1.1.1"
    androidTestImplementation("androidx.test.ext:junit:$extJUnit")
    androidTestImplementation("androidx.test.ext:junit-ktx:$extJUnit")

    val testRunner = "1.1.0"
    androidTestImplementation("androidx.test:runner:$testRunner")

    val testRules = "1.2.0"
    androidTestImplementation("androidx.test:rules:$testRules")

    val espressoCore = "3.2.0"
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoCore")

    val fragment = "1.3.0-alpha06"
    implementation("androidx.fragment:fragment:$fragment")
    debugImplementation("androidx.fragment:fragment-testing:$fragment")

    // Robolectric
    // http://robolectric.org
    val robolectric = "4.3.1"
    testImplementation("org.robolectric:robolectric:$robolectric")

    // Mockk
    // https://github.com/mockk/mockk
    val mockk = "1.9.1"
    testImplementation("io.mockk:mockk:$mockk")
    androidTestImplementation("io.mockk:mockk-android:$mockk")

}

kapt {
    correctErrorTypes = true
}
