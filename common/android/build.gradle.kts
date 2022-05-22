plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = libs.versions.sdk.target.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.asProvider().get()
    }
}

dependencies {
    implementation(project(":common:jvm"))
    implementation(project(":model"))
    implementation(project(":resources"))

    implementation(libs.androidx.appcompat)

    implementation(libs.compose.activity)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)

    implementation(libs.play.core)

    implementation(libs.timber)
}
