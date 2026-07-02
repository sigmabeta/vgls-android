plugins {
    alias(libs.plugins.sage.kmp)
    // BitmapGenerator draws placeholder ImageBitmaps with Compose Multiplatform graphics in commonMain.
    alias(libs.plugins.sage.compose.kmp)
    // The android Coil fetchers/logger are @Inject — apply Metro directly + sage.common.di in androidMain.
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.images"
    }

    sourceSets {
        // Pure, multiplatform: the loading-indicator config/keyer + the Compose-MP placeholder
        // generator. Coil3 core is multiplatform; the concrete android fetchers/decoders live in
        // androidMain (registered into the app's ImageLoader; desktop registers its own/stub).
        named("commonMain") {
            dependencies {
                api(libs.coil.kt.core)
                api(libs.coil.kt.compose)
                api(libs.kotlinx.collections.immutable)
                implementation(libs.sage.common.images)
            }
        }
        named("androidMain") {
            dependencies {
                api(libs.coil.kt.okhttp)
                api(projects.vgls.android.bitmaps)
                implementation(libs.androidx.core.ktx)
                implementation(libs.sage.common.analytics)
                implementation(libs.sage.common.logging)
                implementation(libs.sage.common.di)
            }
        }
    }
}
