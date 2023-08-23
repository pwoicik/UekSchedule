plugins {
    alias(libs.plugins.kgp) apply false
    alias(libs.plugins.agp) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.versions)
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
            freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
        }
    }
}

val ModuleComponentIdentifier.isNotStable: Boolean
    get() {
        val group = group
        val version = version

        if (group == "com.tickaroo.tikxml" && version == "0.8.15") return true
        if (group.startsWith("androidx.compose")) return false
        if (group == "com.google.accompanist") return false

        val stableKeyword =
            listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()

        val isStable = stableKeyword || regex.matches(version)
        return !isStable
    }

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        candidate.isNotStable
    }
}
