plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kgp)
    implementation(libs.hilt.plugin)
}
