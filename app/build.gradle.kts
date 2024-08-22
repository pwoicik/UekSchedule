plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.ksp)
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.compose)
}

android {
    namespace = "com.github.pwoicik.uekschedule"

    compileSdk = libs.versions.sdk.target.get().toInt()

    defaultConfig {
        applicationId = "com.github.pwoicik.uekschedule"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = 38
        versionName = "1.5.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file("debug_key.jks")
            storePassword = "debug_key"
            keyAlias = "key0"
            keyPassword = "debug_key"
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }

        getByName("release") {
            signingConfig = signingConfigs.getByName("release")

            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":common:android"))
    implementation(project(":navigation"))
    implementation(project(":repository"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.lifecycle.runtime)

    implementation(libs.accompanist.uicontroller)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
