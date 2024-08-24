package plugins

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin

class ComposeLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(ComposeCompilerGradleSubplugin::class)

        extensions.configure(CommonExtension::class) {
            buildFeatures.compose = true
        }
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            stabilityConfigurationFile.set(rootDir.resolve("compose_compiler_config.conf"))
            val dump = layout.buildDirectory.dir("compose_compiler")
            metricsDestination.set(dump)
            reportsDestination.set(dump)
        }
    }
}
