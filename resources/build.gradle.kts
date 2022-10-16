plugins {
    id("com.android.library")
}

android {
    namespace = "com.github.pwoicik.uekschedule.resources"
    compileSdk = libs.versions.sdk.target.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
