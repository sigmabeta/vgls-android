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
        maven { url = uri("https://jitpack.io") } // For SupportSQLite
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "VGLeadSheets"

include(
    ":app",

    ":core:android:activity",
    ":core:android:animation",
    ":core:android:bitmaps",
    ":core:android:conversion",
    ":core:android:coroutines",
    ":core:android:database",
    ":core:android:firebase",
    ":core:android:images",
    ":core:android:licenses",
    ":core:android:logging",
    ":core:android:mvrx",
    ":core:android:nav",
    ":core:android:perf",
    ":core:android:pdf",
    ":core:android:repository",
    ":core:android:resources",
    ":core:android:scaffold",
    ":core:android:storage:common",
    ":core:android:tracking",
    ":core:android:ui:components",
    ":core:android:ui:colors",
    ":core:android:ui:icons",
    ":core:android:ui:strings",
    ":core:android:ui:fonts",
    ":core:android:ui:list",
    ":core:android:ui:previews",
    ":core:android:ui:strings",
    ":core:android:ui:themes",
    ":core:android:viewer",
    ":core:android:viewmodel",

    ":core:common:appinfo",
    ":core:common:appcomm",
    ":core:common:conversion",
    ":core:common:coroutines",
    ":core:common:database",
    ":core:common:debug",
    ":core:common:downloader",
    ":core:common:images",
    ":core:common:list",
    ":core:common:logging",
    ":core:common:model",
    ":core:common:nav",
    ":core:common:network",
    ":core:common:notif",
    ":core:common:pdf",
    ":core:common:perf",
    ":core:common:repository",
    ":core:common:storage:common",
    ":core:common:settings:general",
    ":core:common:settings:environment",
    ":core:common:settings:part",
    ":core:common:tracking",
    ":core:common:time",
    ":core:common:ui:components",
    ":core:common:ui:icons",
    ":core:common:ui:strings",
    ":core:common:urlinfo",
    ":core:common:versions",

    ":core:fake:perf",
    ":core:fake:tracking",

    ":features:remaster:all",
    ":features:remaster:browse",
    ":features:remaster:composers:detail",
    ":features:remaster:composers:list",
    ":features:remaster:difficulty:list",
    ":features:remaster:difficulty:values",
    ":features:remaster:games:detail",
    ":features:remaster:games:list",
    ":features:remaster:favorites",
    ":features:remaster:home",
    ":features:remaster:menu",
    ":features:remaster:navbar",
    ":features:remaster:parts",
    ":features:remaster:search",
    ":features:remaster:songs:detail",
    ":features:remaster:songs:list",
    ":features:remaster:tags:list",
    ":features:remaster:tags:songs",
    ":features:remaster:tags:values",
    ":features:remaster:topbar",
    ":features:remaster:updates",
)
