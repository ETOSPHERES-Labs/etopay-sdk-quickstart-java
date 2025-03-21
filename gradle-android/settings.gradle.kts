pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Use Maven Central Snapshots for resolving the etopay sdk
        maven {
            name = "Central Portal Snapshots"
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")

            // Only search this repository for the specific dependency
            content {
                includeModule("com.etospheres.etopay", "etopaysdk")
            }
            mavenContent {
                snapshotsOnly()
            }
        }

    }
}

rootProject.name = "Application"
include(":app")
