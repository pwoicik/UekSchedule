plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

kotlin {
    sourceSets.forEach {
        it.kotlin.srcDir("build/generated/ksp/${it.name}/kotlin")
    }
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "preferences")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.github.pwoicik.uekschedule.features.preferences"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":common:android"))

    implementation(libs.destinations)
    ksp(libs.destinations.ksp)

    implementation(libs.hilt)
    kapt(libs.hilt.kapt)
    implementation(libs.hilt.navigation)
}
