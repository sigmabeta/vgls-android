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
    ":apps:android",
    ":apps:jvm",

    ":vgls:android:activity:real",
    ":vgls:android:bitmaps:real",
    ":vgls:android:icons:real",
    ":vgls:android:ui:theme:api",
    ":vgls:android:analytics:real",
    ":vgls:android:analytics:di",
    ":vgls:android:conversion:real",
    ":vgls:android:conversion:di",
    ":vgls:android:database:real",
    ":vgls:android:database:di",
    ":vgls:android:images:real",
    ":vgls:android:licenses:real",
    ":vgls:android:nav:real",
    ":vgls:android:offline:real",
    ":vgls:android:pdf:real",
    ":vgls:android:repository:di",
    ":vgls:android:scaffold:real",
    ":vgls:android:storage:common:real",
    ":vgls:android:storage:common:di",
    ":vgls:android:ui:components:api",
    ":vgls:android:ui:list:api",
    ":vgls:android:ui:previews:real",
    ":vgls:android:viewmodel:real",
    ":vgls:android:wakelocks:real",
    ":vgls:android:wakelocks:di",

    ":vgls:common:appcomm:api",
    ":vgls:common:appcomm:real",
    ":vgls:common:analytics:api",
    ":vgls:common:analytics:fake",
    ":vgls:common:conversion:api",
    ":vgls:common:environment:api",
    ":vgls:common:strings:api",
    ":vgls:common:database:api",
    ":vgls:common:nav:api",
    ":vgls:common:downloader:api",
    ":vgls:common:downloader:real",
    ":vgls:common:downloader:fake",
    ":vgls:common:model:api",
    ":vgls:common:network:api",
    ":vgls:common:network:real",
    ":vgls:common:network:fake",
    ":vgls:common:notif:api",
    ":vgls:common:notif:real",
    ":vgls:common:offline:api",
    ":vgls:common:offline:real",
    ":vgls:common:repository:real",
    ":vgls:common:settings:part:real",
    ":vgls:common:urlinfo:api",
    ":vgls:common:urlinfo:real",
    ":vgls:common:versions:real",
    ":vgls:common:viewmodel:real",
    ":vgls:common:wakelocks:api",
    ":vgls:common:wakelocks:fake",

    ":features:all:di",
    ":features:browse:real",
    ":features:composers:detail:real",
    ":features:composers:list:real",
    ":features:difficulty:list:real",
    ":features:difficulty:values:real",
    ":features:games:detail:real",
    ":features:games:list:real",
    ":features:favorites:real",
    ":features:home:real",
    ":features:menu:real",
    ":features:navbar:real",
    ":features:offline:content:real",
    ":features:offline:updates:real",
    ":features:parts:real",
    ":features:search:real",
    ":features:songs:detail:real",
    ":features:songs:list:real",
    ":features:tags:list:real",
    ":features:tags:songs:real",
    ":features:tags:values:real",
    ":features:topbar:real",
    ":features:updates:real",
    ":features:viewer:real"
)
