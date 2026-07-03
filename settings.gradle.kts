// Propagate sdk.dir to SAGE submodule so Android Studio can find the SDK when building.
val sageLocalProps = file("sage/local.properties")
if (!sageLocalProps.exists()) {
    val vglsLocalProps = file("local.properties")
    if (vglsLocalProps.exists()) {
        sageLocalProps.writeText(vglsLocalProps.readText())
    }
}

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
            from(files("$settingsDir/sage/gradle/libs.versions.toml"))
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "VGLeadSheets"

include(
    ":app",
    ":apps:jvm",

    ":vgls:android:activity",
    ":vgls:android:bitmaps",
    ":vgls:android:icons",
    ":vgls:android:ui:theme",
    ":vgls:android:analytics",
    ":vgls:fake:analytics",
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
    ":vgls:android:wakelocks",

    ":vgls:common:appcomm",
    ":vgls:common:analytics",
    ":vgls:common:conversion",
    ":vgls:common:environment",
    ":vgls:common:strings",
    ":vgls:common:database",
    ":vgls:common:nav",
    ":vgls:common:downloader",
    ":vgls:common:model",
    ":vgls:common:network",
    ":vgls:common:notif",
    ":vgls:common:offline",
    ":vgls:common:repository",
    ":vgls:common:settings:part",
    ":vgls:common:urlinfo",
    ":vgls:common:versions",
    ":vgls:common:viewmodel",
    ":vgls:common:wakelocks",

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
