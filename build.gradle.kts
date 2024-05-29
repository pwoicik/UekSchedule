import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

plugins {
    alias(libs.plugins.agp) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false

    alias(libs.plugins.versions)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        !isStable(candidate.version) && isStable(currentVersion)
    }
}

fun isStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable
}

subprojects {
    afterEvaluate {
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            stabilityConfigurationFile.set(File(rootDir.absolutePath, "compose_compiler_config.conf"))
            val dump = project.layout.buildDirectory.dir("compose_compiler")
            metricsDestination.set(dump)
            reportsDestination.set(dump)
        }
    }
}
