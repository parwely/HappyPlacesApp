plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.happyplacesapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.happyplacesapp"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}



dependencies {

    implementation (androidx.appcompat:appcompat:1.6.1)
    implementation (com.google.android.material:material:1.8.0)
    implementation (androidx.constraintlayout:constraintlayout:2.1.4)

    // Room Database - Kompatible Versionen für Android 8
    implementation (androidx.room:room-runtime:2.4.3)
    implementation (androidx.room:room-ktx:2.4.3)
    kapt (androidx.room:room-compiler:2.4.3)

    // OpenStreetMap (OSMDroid) - Stabile Version für Android 8
    implementation (org.osmdroid:osmdroid-android:6.1.14)

    // Location Services - Kompatibel mit Android 8
    implementation (com.google.android.gms:play-services-location:20.0.0)

    // Image Loading - Kompatible Version
    implementation (com.github.bumptech.glide:glide:4.14.2)

    // Navigation - Kompatible Versionen
    implementation (androidx.navigation:navigation-fragment-ktx:2.5.3)
    implementation (androidx.navigation:navigation-ui-ktx:2.5.3)

    // ViewModel & LiveData - Android 8 kompatibel
    implementation (androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2)
    implementation (androidx.lifecycle:lifecycle-livedata-ktx:2.6.2)

    // Coroutines - Kompatible Version
    implementation (org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4)

    // Fragment KTX für Android 8
    implementation (androidx.fragment:fragment-ktx:1.5.7)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}