plugins {
    id("com.android.library")
    kotlin("android")
    id("com.google.devtools.ksp")
}

@Suppress("UnstableApiUsage")
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
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    api(project(":common:jvm"))
    api(project(":resources"))

    api(libs.androidx.appcompat)

    api(libs.material)

    api(libs.bundles.compose)

    api(libs.accompanist.flowlayout)

    implementation(libs.destinations)
    ksp(libs.destinations.ksp)

    implementation(libs.play.core)

    api(libs.timber)
}
