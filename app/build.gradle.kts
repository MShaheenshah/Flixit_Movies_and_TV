plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.programmerpro.cricketlivestreaming"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.programmerpro.cricketlivestreaming"
        minSdk = 24
        targetSdk = 34
        versionCode = 10
        versionName = "1.9"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //Retrofit and GSON
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")

    implementation("androidx.compose.ui:ui-android:1.6.0-beta03")

    // extra material icons
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    implementation("com.google.android.material:material:1.10.0")

    // ViewModel in Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    //In-app - Update
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    //Google Ads
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    implementation("com.google.ads.mediation:facebook:6.16.0.0")
    implementation("com.facebook.infer.annotation:infer-annotation:0.18.0")

    //One Signal
    //noinspection GradleDependency
    implementation("com.onesignal:OneSignal:[5.0.0, 5.99.99]")

    //Firebase
    implementation("com.google.firebase:firebase-database:20.3.0")
//    implementation("com.google.firebase:firebase-storage:20.3.0")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    // JSON parser
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")

    // Media3
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")
    implementation("androidx.media3:media3-exoplayer-hls:1.2.0")
//    implementation("com.google.android.exoplayer:exoplayer-hls")

    // SplashScreen
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0-alpha01")
    ksp("com.google.dagger:hilt-android-compiler:2.49")

    // Baseline profile installer
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Compose Previews
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")
}