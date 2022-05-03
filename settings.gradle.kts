pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "UEK Schedule"

include(
    ":app",
    ":common",
    ":model",
    ":db",
    ":api",
    ":repository"
)
