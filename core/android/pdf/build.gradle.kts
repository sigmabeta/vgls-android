plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(libs.coil.kt)
}

android {
    namespace = "com.vgleadsheets.pdf"
}
