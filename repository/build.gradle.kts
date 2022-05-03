plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = libs.versions.sdk.target.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
}

dependencies {
    implementation(project(":common"))
    implementation(project(":model"))
    implementation(project(":api"))
    implementation(project(":db"))

    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.ksp)

    implementation(libs.timber)

    implementation(libs.dagger)
    kapt(libs.dagger.kapt)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.android)
    testImplementation(libs.mockito)
    testImplementation(libs.retrofit)
    testImplementation(libs.okhttp.tls)
    testImplementation(libs.tikxml.core)
    testImplementation(libs.tikxml.retrofit)
}
