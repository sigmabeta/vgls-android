plugins {
    alias(libs.plugins.sage.kmp)
    // subsample/CompositionLocal + PdfSubsampleSource (androidMain) use Compose runtime/graphics.
    alias(libs.plugins.sage.compose.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.pdf"
    }

    sourceSets {
        // Pure Coil plumbing: cache keys/ZOOM const, the PDF Coil keyer/fetcher/metadata. These are
        // multiplatform (Coil3 + okio). The android.graphics/PdfRenderer + telephoto subsampling
        // decoders live in androidMain; desktop registers its own/stub Coil decoder.
        named("commonMain") {
            dependencies {
                // telephoto is multiplatform; LocalPdfSubsampler/PdfSubsampleSourceFactory return its
                // SubSamplingImageSource so the shared ZoomableSheet can build one.
                api(libs.zoomable.image.coil3)
                api(libs.sage.common.pdf)
                implementation(libs.coil.kt.core)
                implementation(libs.okio)
                implementation(projects.vgls.common.downloader)
                implementation(projects.vgls.common.urlinfo)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.coil.kt.core)
                implementation(libs.androidx.core.ktx)
                implementation(projects.vgls.android.bitmaps)
                implementation(libs.sage.common.debug)
                implementation(libs.sage.common.logging)
                implementation(projects.vgls.common.repository)
            }
        }
    }
}
