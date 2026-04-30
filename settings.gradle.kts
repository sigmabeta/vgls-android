includeBuild("sage/sage-build-logic")
includeBuild("build-logic")
includeBuild("sage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
        } // Gradle license plugin snapshot
    }
}

plugins {
    id("com.gradle.develocity") version "3.17.3"
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
    }
}

buildCache {
    local {
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // For SupportSQLite
        maven {
            url = uri("https://jitpack.io")
        }

        // For Telephoto
        maven {
            setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
    versionCatalogs {
        create("libs") {
            from(files("sage/gradle/libs.versions.toml"))
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "VGLeadSheets"

include(
    ":app",

    ":vgls:android:activity",
    ":vgls:android:conversion",
    ":vgls:android:database",
    ":vgls:android:images",
    ":vgls:android:licenses",
    ":vgls:android:nav",
    ":vgls:android:offline",
    ":vgls:android:pdf",
    ":vgls:android:repository",
    ":vgls:android:scaffold",
    ":vgls:android:storage:common",
    ":vgls:android:ui:components",
    ":vgls:android:ui:list",
    ":vgls:android:ui:previews",
    ":vgls:android:viewmodel",

    ":vgls:common:conversion",
    ":vgls:common:database",
    ":vgls:common:downloader",
    ":vgls:common:model",
    ":vgls:common:network",
    ":vgls:common:notif",
    ":vgls:common:offline",
    ":vgls:common:repository",
    ":vgls:common:settings:part",
    ":vgls:common:urlinfo",
    ":vgls:common:versions",

    ":features:all",
    ":features:browse",
    ":features:composers:detail",
    ":features:composers:list",
    ":features:difficulty:list",
    ":features:difficulty:values",
    ":features:games:detail",
    ":features:games:list",
    ":features:favorites",
    ":features:home",
    ":features:menu",
    ":features:navbar",
    ":features:offline",
    ":features:offline:content",
    ":features:offline:updates",
    ":features:parts",
    ":features:search",
    ":features:songs:detail",
    ":features:songs:list",
    ":features:tags:list",
    ":features:tags:songs",
    ":features:tags:values",
    ":features:topbar",
    ":features:updates",
    ":features:viewer"
)
