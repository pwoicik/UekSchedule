plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {

    implementation(project(":common:jvm"))
    implementation(libs.retrofit)

    implementation(libs.tikxml.core)
    kapt(libs.tikxml.kapt)
    implementation(libs.tikxml.retrofit)
    implementation(libs.tikxml.annotation)
    implementation(libs.tikxml.htmlescape)

    implementation(libs.dagger)
    kapt(libs.dagger.kapt)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
