pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    // Konfigurasi untuk menggunakan JDK 17
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version("0.5.0")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TodoApp"
include(":app")
 