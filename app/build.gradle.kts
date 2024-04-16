plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    //Google services
    id("com.google.gms.google-services")
}

android {
    namespace = "com.sgr.glucomaster"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sgr.glucomaster"
        minSdk = 23
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    //ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Implementation SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")
    //Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    //Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")
    //Google Play services library
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    //Google Analytics
    implementation("com.google.firebase:firebase-analytics")
}