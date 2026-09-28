buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

// Lists all plugins used throughout the project without applying them.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.git.version) apply false
    alias(libs.plugins.gradle.publisher) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.paparazzi) apply false
}

// ktlint via the Gradle plugin, replacing the old ktlint-check.sh that downloaded the ktlint binary
// and ran it outside Gradle. Applied to every VGLS module; the vendored sage/ submodule is a separate
// included build and keeps its own lint config, so it's untouched here. `ktlintCheck` (wired into
// `check`) lints; `ktlintFormat` auto-fixes. The engine is pinned to the version the old script used
// so the active ruleset doesn't change.
val ktlintToolVersion = libs.versions.ktlintTool.get()
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set(ktlintToolVersion)
    }
    // KSP/Room and Compose Multiplatform register their generated sources (e.g. VglsDatabase_Impl.kt,
    // the Compose `Res` accessors) into the Kotlin source sets, so ktlint-gradle would lint them. Keep
    // the old script's `**/build/**` exclusion so we only lint hand-written code.
    tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask>().configureEach {
        exclude { it.file.path.contains("/build/") }
    }
    // Mirror the exclusion for detekt: Compose-resource codegen produces source files (Res.kt, the
    // generated string accessors) that violate detekt rules. The predicate form catches the absolute-
    // path source entries Compose's resourceGenerator registers — `exclude("**/build/**")` only matches
    // paths relative to the source roots and misses them.
    plugins.withId("io.gitlab.arturbosch.detekt") {
        tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
            exclude { it.file.path.contains("/build/") }
        }
    }
}
