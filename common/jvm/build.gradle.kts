plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    api(project(":model"))

    api(libs.kotlinx.coroutines)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
