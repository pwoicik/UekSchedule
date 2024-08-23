plugins {
    id("com.android.library")
    kotlin("android")
    id("com.google.devtools.ksp")
    alias(libs.plugins.compose)
}

android {
    namespace = "com.github.pwoicik.uekschedule.common"
    compileSdk = libs.versions.sdk.target.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
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
}

dependencies {
    api(project(":common:jvm"))
    api(project(":resources"))

    api(libs.androidx.appcompat)

    api(libs.material)

    api(libs.bundles.compose)

    implementation(libs.destinations)
    ksp(libs.destinations.ksp)

    implementation(libs.play.review)

    api(libs.timber)
}
