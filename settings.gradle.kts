includeBuild("build-logic")

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
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "VGLeadSheets"

include(
    ":app",

    ":core:android:activity",
    ":core:android:bitmaps",
    ":core:android:conversion",
    ":core:android:coroutines",
    ":core:android:database",
    ":core:android:firebase",
    ":core:android:images",
    ":core:android:licenses",
    ":core:android:logging",
    ":core:android:nav",
    ":core:android:offline",
    ":core:android:perf",
    ":core:android:pdf",
    ":core:android:repository",
    ":core:android:resources",
    ":core:android:scaffold",
    ":core:android:storage:common",
    ":core:android:analytics",
    ":core:android:ui:components",
    ":core:android:ui:colors",
    ":core:android:ui:icons",
    ":core:android:ui:strings",
    ":core:android:ui:fonts",
    ":core:android:ui:list",
    ":core:android:ui:previews",
    ":core:android:ui:strings",
    ":core:android:ui:themes",
    ":core:android:viewmodel",
    ":core:android:wakelocks",

    ":core:common:analytics",
    ":core:common:appinfo",
    ":core:common:appcomm",
    ":core:common:conversion",
    ":core:common:coroutines",
    ":core:common:database",
    ":core:common:debug",
    ":core:common:downloader",
    ":core:common:events",
    ":core:common:images",
    ":core:common:list",
    ":core:common:logging",
    ":core:common:model",
    ":core:common:nav",
    ":core:common:network",
    ":core:common:notif",
    ":core:common:offline",
    ":core:common:pdf",
    ":core:common:perf",
    ":core:common:repository",
    ":core:common:storage:common",
    ":core:common:settings:general",
    ":core:common:settings:environment",
    ":core:common:settings:part",
    ":core:common:time",
    ":core:common:ui:components",
    ":core:common:ui:icons",
    ":core:common:ui:strings",
    ":core:common:urlinfo",
    ":core:common:versions",
    ":core:common:wakelocks",

    ":core:fake:perf",
    ":core:fake:analytics",

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
    ":features:parts",
    ":features:search",
    ":features:songs:detail",
    ":features:songs:list",
    ":features:tags:list",
    ":features:tags:songs",
    ":features:tags:values",
    ":features:topbar",
    ":features:updates",
    ":features:viewer",
)
