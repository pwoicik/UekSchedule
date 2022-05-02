plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "com.github.pwoicik.uekschedule"

    compileSdk = libs.versions.sdk.target.get().toInt()

    defaultConfig {
        applicationId = "com.github.pwoicik.uekschedule"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = 26
        versionName = "1.2.3.2"

        testInstrumentationRunner = "com.github.pwoicik.uekschedule.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
//            debuggable = true
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.asProvider().get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.icons)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.destinations)
    ksp(libs.destinations.ksp)
    implementation(libs.accompanist.uicontroller)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.ksp)
    implementation(libs.datastore)
    implementation(libs.retrofit)
    implementation(libs.tikxml.core)
    kapt(libs.tikxml.kapt)
    implementation(libs.tikxml.retrofit)
    implementation(libs.tikxml.annotation)
    implementation(libs.tikxml.htmlescape)
    implementation(libs.hilt)
    kapt(libs.hilt.kapt)
    implementation(libs.hilt.navigation)
    implementation(libs.timber)
    implementation(libs.play.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.android)
    androidTestImplementation(libs.espresso)
    testImplementation(libs.mockito)
    androidTestImplementation(libs.compose.test)
    testImplementation(libs.okhttp.tls)
    testImplementation(libs.hilt.test)
    androidTestImplementation(libs.hilt.test)
    debugImplementation(libs.compose.tooling)
}
