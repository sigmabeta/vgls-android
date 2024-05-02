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
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.gradle.publisher) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.jvm) apply false

    alias(libs.plugins.vgls.android.app) apply false
    alias(libs.plugins.vgls.compose.android.app) apply false
    alias(libs.plugins.vgls.compose.android.module) apply false
    alias(libs.plugins.vgls.feature.android) apply false
    alias(libs.plugins.vgls.feature.compose.android) apply false
    alias(libs.plugins.vgls.core.android) apply false
    alias(libs.plugins.vgls.core.jvm) apply false
    alias(libs.plugins.vgls.di.android) apply false
    alias(libs.plugins.vgls.di.jvm) apply false
}
