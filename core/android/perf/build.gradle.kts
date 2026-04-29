plugins {
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    api(libs.sage.common.logging)
}

android {
    namespace = "com.vgleadsheets.perf"

    buildFeatures {
        buildConfig = true
    }
}
