pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()   // <-- needed so KSP can be resolved
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "CP3406-reciping"
include(":app")
