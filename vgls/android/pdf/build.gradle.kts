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
                api(libs.sage.common.pdf)
                implementation(libs.coil.kt.core)
                implementation(libs.okio)
                implementation(projects.vgls.common.downloader)
                implementation(projects.vgls.common.urlinfo.real)
            }
        }
        named("androidMain") {
            dependencies {
                // telephoto publishes no JVM variant (aar-only), and its subsampling viewer needs an
                // android PdfRenderer bitmap — so LocalPdfSubsampler/PdfSubsampleSourceFactory (which
                // return its SubSamplingImageSource) are android-only. api() so consumers' androidMain
                // (ui/components' ZoomableSheet) see the return type.
                api(libs.zoomable.image.coil3)
                implementation(libs.coil.kt.core)
                implementation(libs.androidx.core.ktx)
                implementation(projects.vgls.android.bitmaps)
                implementation(libs.sage.common.debug)
                implementation(libs.sage.common.logging)
                implementation(projects.vgls.common.repository.real)
            }
        }
    }
}
