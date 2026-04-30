includeBuild("../sage") {
    dependencySubstitution {
        substitute(module("net.sigmabeta.sage:common-analytics")).using(project(":common:analytics"))
        substitute(module("net.sigmabeta.sage:common-appcomm")).using(project(":common:appcomm"))
        substitute(module("net.sigmabeta.sage:common-appinfo")).using(project(":common:appinfo"))
        substitute(module("net.sigmabeta.sage:common-connectivity")).using(project(":common:connectivity"))
        substitute(module("net.sigmabeta.sage:common-storage-common")).using(project(":common:storage:common"))
        substitute(module("net.sigmabeta.sage:common-coroutines")).using(project(":common:coroutines"))
        substitute(module("net.sigmabeta.sage:common-debug")).using(project(":common:debug"))
        substitute(module("net.sigmabeta.sage:common-events")).using(project(":common:events"))
        substitute(module("net.sigmabeta.sage:common-images")).using(project(":common:images"))
        substitute(module("net.sigmabeta.sage:common-list")).using(project(":common:list"))
        substitute(module("net.sigmabeta.sage:common-logging")).using(project(":common:logging"))
        substitute(module("net.sigmabeta.sage:common-nav")).using(project(":common:nav"))
        substitute(module("net.sigmabeta.sage:common-pdf")).using(project(":common:pdf"))
        substitute(module("net.sigmabeta.sage:common-perf")).using(project(":common:perf"))
        substitute(module("net.sigmabeta.sage:common-settings-environment")).using(project(":common:settings:environment"))
        substitute(module("net.sigmabeta.sage:common-settings-general")).using(project(":common:settings:general"))
        substitute(module("net.sigmabeta.sage:common-time")).using(project(":common:time"))
        substitute(module("net.sigmabeta.sage:common-ui-components")).using(project(":common:ui:components"))
        substitute(module("net.sigmabeta.sage:common-ui-icons")).using(project(":common:ui:icons"))
        substitute(module("net.sigmabeta.sage:common-ui-strings")).using(project(":common:ui:strings"))
        substitute(module("net.sigmabeta.sage:common-wakelocks")).using(project(":common:wakelocks"))
        substitute(module("net.sigmabeta.sage:android-analytics")).using(project(":android:analytics"))
        substitute(module("net.sigmabeta.sage:android-bitmaps")).using(project(":android:bitmaps"))
        substitute(module("net.sigmabeta.sage:android-connectivity")).using(project(":android:connectivity"))
        substitute(module("net.sigmabeta.sage:android-coroutines")).using(project(":android:coroutines"))
        substitute(module("net.sigmabeta.sage:android-firebase")).using(project(":android:firebase"))
        substitute(module("net.sigmabeta.sage:android-logging")).using(project(":android:logging"))
        substitute(module("net.sigmabeta.sage:android-perf")).using(project(":android:perf"))
        substitute(module("net.sigmabeta.sage:android-resources")).using(project(":android:resources"))
        substitute(module("net.sigmabeta.sage:android-ui-colors")).using(project(":android:ui:colors"))
        substitute(module("net.sigmabeta.sage:android-ui-fonts")).using(project(":android:ui:fonts"))
        substitute(module("net.sigmabeta.sage:android-ui-icons")).using(project(":android:ui:icons"))
        substitute(module("net.sigmabeta.sage:android-ui-strings")).using(project(":android:ui:strings"))
        substitute(module("net.sigmabeta.sage:android-ui-themes")).using(project(":android:ui:themes"))
        substitute(module("net.sigmabeta.sage:android-wakelocks")).using(project(":android:wakelocks"))
        substitute(module("net.sigmabeta.sage:fake-analytics")).using(project(":fake:analytics"))
        substitute(module("net.sigmabeta.sage:fake-perf")).using(project(":fake:perf"))
    }
}
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
    versionCatalogs {
        create("libs") {
            from(files("../sage/gradle/libs.versions.toml"))
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
