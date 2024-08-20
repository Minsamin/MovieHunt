import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.rpimx.moviehunt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rpimx.moviehunt"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.rpimx.moviehunt.CustomRunnerTest"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        val pp = Properties()
        pp.load(project.rootProject.file("local.properties").inputStream())
        // Access the API_KEY from properties
        val apiKey = pp.getProperty("TMDB_API_KEY")



        debug {

            buildConfigField("String", "API_KEY", "\"${apiKey}\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            //val apiKey: String = gradleLocalProperties(rootDir, providers).getProperty("API_KEY") ?: ""
            buildConfigField("String", "API_KEY", "\"$apiKey\"")

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/*{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.arch.core)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.kotlinx.serialization.json)

    // Dagger Hilt
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.dagger.hilt.android.compiler)

    // Pagination
    implementation(libs.androidx.paging.common.android)
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Compose dependencies
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.flowlayout)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Navigation animation
    implementation(libs.accompanist.navigation.animation)

    // Accompanist System UI controller
    implementation(libs.accompanist.systemuicontroller)

    // Swipe to refresh
    implementation(libs.accompanist.swiperefresh)

    // DataStore
    implementation(libs.datastore.preferences)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.retrofit.logging.interceptor)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.kotlinxSerialization)

    // Async Image Loader
    implementation(libs.coil)


    

    // Test Dependencies

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Android, Kotlin and Coroutine testing
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.arch.runtime)
    androidTestImplementation(libs.androidx.arch.common)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.core.ktx)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.test.coroutines)

    //Paging testing
    testImplementation(libs.androidx.paging.common)
    testImplementation(libs.androidx.paging.testing)

    //Room testing
    testImplementation(libs.room.testing)
    testImplementation (libs.androidx.test.runner)

    //Hilt Testing
    kaptAndroidTest(libs.dagger.hilt.android.testing)
    kaptAndroidTest(libs.dagger.hilt.android.compiler)
    kaptAndroidTest(libs.dagger.hilt.android.testing)
    kaptTest(libs.dagger.hilt.android.compiler)
    kaptTest(libs.dagger.hilt.android.testing)
    androidTestImplementation(libs.androidx.hilt.navigation.compose)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    androidTestAnnotationProcessor(libs.dagger.hilt.android.compiler)
    testImplementation(libs.dagger.hilt.android.testing)

    // Mock testing
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.room.testing)
    testImplementation(libs.mockito)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
}