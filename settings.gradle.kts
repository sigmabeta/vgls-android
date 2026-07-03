import org.gradle.caching.http.HttpBuildCache

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
    // Develocity Build Scans. The `sage` submodule pins its own (4.4.2) for standalone builds,
    // but in this composite build the root build owns the scan and sage's plugin defers to it — so
    // this must stay on the latest Gradle-9-compatible line (kept in sync with sage's version).
    id("com.gradle.develocity") version "4.4.2"
}

develocity {
    buildScan {
        // Free public Build Scan service (scans.gradle.com) — accept its terms non-interactively.
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"

        // Auto-publish on CI only — CircleCI exports CI in the environment. Locally nothing is
        // uploaded unless you pass `--scan`, which overrides this predicate and always publishes.
        // Reading via providers keeps it configuration-cache safe.
        val isCi = providers.environmentVariable("CI").isPresent
        publishing.onlyIf { isCi }
        // Tag the CI-published scans so they're filterable apart from any local `--scan` runs.
        if (isCi) tag("CI")
    }
}

buildCache {
    // Local cache: fast within-build / same-machine reuse. CircleCI persists this directory across
    // jobs via save_cache/restore_cache, so keep it at the repo-relative `build-cache` path.
    local {
        directory = File(rootDir, "build-cache")
    }
    // Remote: the self-hosted gradle/build-cache-node behind Caddy (TLS) at sebacloud.org.
    remote<HttpBuildCache> {
        setUrl("https://gradle.sebacloud.org/cache/")

        // Trust boundary — only CI pushes; everyone else reads anonymously. The node grants
        // anonymous read, so local dev needs no credentials. CircleCI exports CI in the env;
        // pushing additionally requires the write password, so a misconfigured CI can't half-push.
        // All reads go through providers to stay configuration-cache safe.
        val ciPassword = providers.environmentVariable("GRADLE_CACHE_PASSWORD")
        isPush = providers.environmentVariable("CI").isPresent && ciPassword.isPresent
        if (isPush) {
            credentials {
                // GRADLE_CACHE_USER is optional — the node's write user is "ci". It can arrive
                // present-but-blank from an unset CI secret, so treat blank as unset.
                username = providers.environmentVariable("GRADLE_CACHE_USER").orElse("ci").get().ifBlank { "ci" }
                password = ciPassword.get()
            }
        }
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
    ":vgls:android:ui:fonts:real",
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
