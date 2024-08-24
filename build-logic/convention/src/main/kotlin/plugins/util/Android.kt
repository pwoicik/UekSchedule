package plugins.util

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion

internal const val MinSdk = 26
internal const val TargetSdk = 34

internal fun CommonExtension<*, *, *, *, *, *>.configureJava() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
