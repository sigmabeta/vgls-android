plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    api(libs.sage.common.logging)
}

android {
    namespace = "com.vgleadsheets.logging"

    buildFeatures {
        buildConfig = true
    }
}
