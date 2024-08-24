plugins {
    alias(libs.plugins.internal.androidApp)
    alias(libs.plugins.internal.composeLib)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
    id("kotlin-parcelize")
}

ksp {
    arg("lyricist.generateStringsProperty", "true")
}

sqldelight {
    databases {
        create("Database") {
            packageName = "uekschedule.data.db"
            schemaOutputDirectory = file("src/main/sqldelight/schema")
            verifyMigrations = true
        }
    }
}

android {
    namespace = "com.github.pwoicik.uekschedule"

    defaultConfig {
        applicationId = "com.github.pwoicik.uekschedule"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    kotlinOptions {
        options.optIn.addAll(
            "kotlinx.coroutines.FlowPreview",
            "androidx.compose.foundation.ExperimentalFoundationApi",
            "androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "androidx.compose.material3.ExperimentalMaterial3Api",
        )
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.logcat)
    implementation(libs.koin.android)
    implementation(libs.circuit.foundation)
    implementation(libs.circuit.overlay)
    implementation(libs.circuitx.android)
    implementation(libs.circuitx.overlays)
    implementation(libs.circuitx.gestureNavigation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization.xml)
    implementation(libs.kotlin.datetime)
    implementation(libs.lyricist)
    ksp(libs.lyricist.processor)
    implementation(libs.arrow.core)
    implementation(libs.arrow.fx)
    implementation(libs.sqldelight.androidDriver)
    implementation(libs.sqldelight.coroutines)
}
