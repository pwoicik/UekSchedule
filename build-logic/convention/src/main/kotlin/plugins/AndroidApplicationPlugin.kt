package plugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import plugins.util.MinSdk
import plugins.util.TargetSdk
import plugins.util.configureJava
import plugins.util.configureKotlin

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(AppPlugin::class)
        pluginManager.apply(KotlinAndroidPluginWrapper::class)

        extensions.configure<ApplicationExtension> {
            compileSdk = TargetSdk
            defaultConfig {
                minSdk = MinSdk
                targetSdk = TargetSdk

                vectorDrawables {
                    useSupportLibrary = true
                }
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }

            configureJava()
        }

        configureKotlin()
    }
}
