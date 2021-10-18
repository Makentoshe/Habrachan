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
    compileSdkVersion(dependency.build.compileSdkVersion)
    defaultConfig {
        applicationId = "com.makentoshe.habrachan"
        minSdkVersion(dependency.build.minSdkVersion)
        targetSdkVersion(dependency.build.targetSdkVersion)
        versionCode = dependency.build.versionCode
        versionName = dependency.build.versionName
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
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*,jar"))))

    implementation(kotlin("stdlib"))

    implementation(project(":habrachan"))
    implementation(project(":habrachan:api"))
    implementation(project(":habrachan:api:mobile"))
    implementation(project(":habrachan:network"))
    implementation(project(":habrachan:network:articles-get"))
    implementation(project(":habrachan:network:articles-get:mobile"))

    implementation(project(":application:core"))
    implementation(project(":application:common:arena:comments"))
    implementation(project(":application:common:arena:content"))
    implementation(project(":application:android:core"))
    implementation(project(":application:android:database"))
    implementation(project(":application:android:analytics"))

    implementation(project(":application:android:screen"))
    implementation(project(":application:android:screen:comments"))
    implementation(project(":application:android:screen:comments:article"))
    implementation(project(":application:android:screen:comments:thread"))
    implementation(project(":application:android:screen:comments:dispatch"))
    implementation(project(":application:android:screen:articles"))
    implementation(project(":application:android:screen:articles:flow"))
    implementation(project(":application:android:screen:articles:page"))
    implementation(project(":application:android:screen:article"))

    implementation(project(":application:android:navigation"))
    implementation(project(":application:android:filesystem"))
    implementation(project(":application:android:common:arena"))
    implementation(project(":application:android:common:avatar"))
    implementation(project(":application:android:common"))
    implementation(project(":application:android:common:comment"))
    implementation(project(":application:android:di"))

    implementation(project(":entity"))
    implementation(project(":entity:entity-native"))
    implementation(project(":entity:entity-mobile"))

    implementation(project(":network"))
    implementation(project(":network:network-native"))
    implementation(project(":network:network-native:network-native-common"))
    implementation(project(":network:network-native:network-native-login"))
    implementation(project(":network:network-native:network-native-article-vote"))
    implementation(project(":network:network-native:network-native-article-get"))
    implementation(project(":network:network-native:network-native-articles-get"))
    implementation(project(":network:network-native:network-native-comment-vote"))
    implementation(project(":network:network-native:network-native-comment-post"))
    implementation(project(":network:network-native:network-native-comments-get"))
    implementation(project(":network:network-native:network-native-user-get"))
    implementation(project(":network:network-native:network-native-user-me"))
    implementation(project(":network:network-mobile"))
    implementation(project(":network:network-mobile:network-mobile-login"))

    implementation(project(":network:network-common:network-common-content"))

    implementation(project(":functional"))

    // OkHttp
    // https://github.com/square/okhttp/
    val okhttpVersion = dependency.version.okhttp
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

    // Ktor client
    val ktorHttpClientVersion = dependency.version.ktorHttpClient
    implementation("io.ktor:ktor-client-core:$ktorHttpClientVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorHttpClientVersion")

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

    // Cicerone
    // https://github.com/terrakok/Cicerone
    val ciceroneVersion = dependency.version.cicerone
    implementation("ru.terrakok.cicerone:cicerone:$ciceroneVersion")

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

    // Recycler view additional classes such as ConcatAdapter
    // https://developer.android.com/jetpack/androidx/releases/recyclerview
    val recyclerViewVersion = dependency.version.androidRecyclerView
    implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")

    // SlidingUpPanelLayout
    // https://github.com/umano/AndroidSlidingUpPanel
    val slidingUpPanelVersion = dependency.version.androidSlidingUpPanel
    implementation("com.sothree.slidinguppanel:library:$slidingUpPanelVersion")

    // Xml parsing
    // https://github.com/jhy/jsoup
    val jsoupVersion = dependency.version.jsoup
    implementation("org.jsoup:jsoup:$jsoupVersion")

    // Dexter (request permissions)
    // https://github.com/Karumi/Dexter/
    val dexterVersion = dependency.version.androidDexter
    implementation("com.karumi:dexter:$dexterVersion")

    // Room
    val roomVersion = dependency.version.androidRoom
    implementation("android.arch.persistence.room:runtime:$roomVersion")
    kapt("android.arch.persistence.room:compiler:$roomVersion")

    // Pagination library
    // https://developer.android.com/topic/libraries/architecture/paging
    val paginationVersion = dependency.version.androidPaging
    implementation("androidx.paging:paging-runtime-ktx:$paginationVersion")
    implementation("androidx.paging:paging-rxjava2:$paginationVersion")

    // Subsampling scale image view
    // https://github.com/davemorrissey/subsampling-scale-image-view
    val subsamplingScaleImageViewVersion = dependency.version.androidSubsamplingScaleImageView
    implementation("com.davemorrissey.labs:subsampling-scale-image-view:$subsamplingScaleImageViewVersion")

    val gif = dependency.version.androidGifDrawable
    implementation("pl.droidsonroids.gif:android-gif-drawable:$gif")

    // Multiline Collapsing Toolbar
    // https://github.com/opacapp/multiline-collapsingtoolbar
    val collapsing = dependency.version.androidCollapsingToolbar
    implementation("net.opacapp:multiline-collapsingtoolbar:$collapsing")

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

    val testCoreVersion = dependency.version.androidTestCore
    androidTestImplementation("androidx.test:core:$testCoreVersion")
    androidTestImplementation("androidx.test:core-ktx:$testCoreVersion")

    val extJUnitVersion = dependency.version.androidTestJunit
    androidTestImplementation("androidx.test.ext:junit:$extJUnitVersion")
    androidTestImplementation("androidx.test.ext:junit-ktx:$extJUnitVersion")

    val testRunnerVersion = dependency.version.androidTestRunner
    androidTestImplementation("androidx.test:runner:$testRunnerVersion")

    val testRulesVersion = dependency.version.androidTestRules
    androidTestImplementation("androidx.test:rules:$testRulesVersion")

    val espressoCoreVersion = dependency.version.androidTestEspresso
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoCoreVersion")

    val fragmentVersion = dependency.version.androidFragment
    implementation("androidx.fragment:fragment:$fragmentVersion")
    debugImplementation("androidx.fragment:fragment-testing:$fragmentVersion")

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

kapt {
    correctErrorTypes = true
}
