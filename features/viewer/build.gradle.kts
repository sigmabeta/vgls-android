plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.ui.viewer"
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(projects.vgls.common.analytics.api)
                api(libs.androidx.lifecycle.runtimeCompose)
                api(projects.vgls.common.wakelocks.api)
                api(projects.vgls.common.nav.api)
                implementation(projects.vgls.android.pdf)
                implementation(projects.vgls.android.ui.components)
                implementation(projects.vgls.android.viewmodel)
                implementation(projects.vgls.android.bitmaps)
                implementation(libs.sage.common.appcomm)
                implementation(projects.vgls.common.appcomm.api)
                implementation(libs.sage.common.ui.iconsReal)
                implementation(libs.sage.common.pdf)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.metrox.viewmodel)
                implementation(libs.metrox.viewmodel.compose)
                implementation(projects.vgls.common.strings)
                implementation(libs.sage.common.di)
            }
        }
        named("androidMain") {
            dependencies {
                // The real (android-only) sheet renderer lives here: SheetPager + the ViewerScreen
                // actual drive telephoto's ZoomSpec/ZoomableState (aar-only; no JVM variant). The
                // JVM actual is a placeholder, so commonMain stays telephoto-free.
                implementation(libs.zoomable.image.coil3)
                implementation(projects.vgls.android.ui.theme)
                implementation(libs.androidx.compose.ui.tooling.preview)
            }
        }
    }
}
