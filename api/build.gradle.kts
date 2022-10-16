plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {

    implementation(project(":common:jvm"))

    implementation(libs.retrofit)

    implementation(libs.bundles.tikxml)
    kapt(libs.tikxml.kapt)

    implementation(libs.dagger)
    kapt(libs.dagger.kapt)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
