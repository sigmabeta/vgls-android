import org.jetbrains.compose.resources.ResourcesExtension

plugins {
    alias(libs.plugins.sage.kmp)
    alias(libs.plugins.sage.compose.kmp)
    // composeResources for the blank-staff placeholder drawable used by PreviewSheet.
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.ui.components"
        // composeResources (drawable) + the androidMain manifest theme need R/resource processing,
        // which AGP9's KMP android library defaults OFF.
        androidResources {
            enable = true
        }
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(libs.sage.common.ui.components)
                implementation(projects.vgls.common.appcomm)
                implementation(projects.vgls.common.model)
                implementation(projects.vgls.android.bitmaps)
                implementation(projects.vgls.android.images)
                implementation(projects.vgls.android.pdf)
                // telephoto (multiplatform) for the shared ZoomableSheet sub-sampling viewer.
                implementation(libs.zoomable.image.coil3)
                implementation(libs.sage.common.ui.perfCompose)
                implementation(libs.sage.common.ui.iconsReal)
                implementation(projects.vgls.android.ui.theme)
                implementation(projects.vgls.common.strings)
                implementation(libs.jetbrains.compose.resources)
            }
        }
        // The 4 PDF-renderer composables (ZoomableSheet/ZoomableFullDoc/PdfDisplayer/
        // FakeBitmapDisplayer) drive pdf's androidMain renderer; @Preview functions (extracted from
        // the commonMain composables) also live here (android tooling @Preview).
        named("androidMain") {
            dependencies {
                implementation(libs.androidx.compose.ui.tooling.preview)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = ResourcesExtension.ResourceClassGeneration.Always
    packageOfResClass = "com.vgleadsheets.ui.components.generated.resources"
}
