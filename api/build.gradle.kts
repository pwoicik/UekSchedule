plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("kapt")
    alias(libs.plugins.ksp)
}

dependencies {

    implementation(project(":common:jvm"))

    implementation(libs.retrofit)

    implementation(libs.bundles.tikxml)
    kapt(libs.tikxml.kapt)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
