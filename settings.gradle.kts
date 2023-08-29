pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "EventCademy"
include(":app")
include(":core:data-firebase")
include(":core:domain")
include(":core:data-preferences")
include(":core:testing")
include(":feature:auth")
include(":feature:bookmark")
include(":feature:createevent")
include(":feature:eventdetail")
include(":feature:home")
include(":feature:profile")
include(":feature:setting")
include(":core:designsystem")
include(":core:util")
include(":core:ui")
