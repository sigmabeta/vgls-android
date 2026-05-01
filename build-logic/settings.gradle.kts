dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../sage/gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")

buildCache {
    local {
        directory = File(rootDir, "build-cache")
    }
}
