plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tracer.overlay"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tracer.overlay"
        minSdk = 26          // TYPE_APPLICATION_OVERLAY requires API 26+
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // --- 32-BIT TARGETING (per requirement #1) ---
        // Restricts native/JNI packaging to 32-bit ABIs only. Since this app
        // ships no native code, this mainly guarantees that if any future
        // dependency adds .so files, only 32-bit variants are packaged.
        ndk {
            abiFilters += listOf("armeabi-v7a", "x86")
        }
    }

    // Build one small APK per 32-bit ABI instead of a single fat/universal APK.
    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "x86")
            isUniversalApk = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
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
        viewBinding = true
    }

    // Keep the codebase lean: no ad SDKs, no analytics, no heavy libs.
    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    // Deliberately minimal — no ad networks, no analytics, no heavy UI kits.
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.2")
}
