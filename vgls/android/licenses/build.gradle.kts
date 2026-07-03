plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.ui.licenses"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.androidx.lifecycle.runtimeCompose)
                api(libs.sage.common.appcomm)
                api(projects.vgls.common.nav.api)
                implementation(projects.vgls.android.ui.components)
                implementation(projects.vgls.common.strings.api)
                implementation(projects.vgls.android.viewmodel)
                implementation(libs.metrox.viewmodel)
                implementation(libs.metrox.viewmodel.compose)
                implementation(libs.sage.common.di)
            }
        }
        named("androidMain") {
            dependencies {
                // The android WebView-backed LicenseScreen actual.
                api(libs.webview)
            }
        }
    }
}
