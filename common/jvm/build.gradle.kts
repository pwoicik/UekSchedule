plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    implementation(project(":model"))

    implementation(libs.kotlinx.coroutines)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
