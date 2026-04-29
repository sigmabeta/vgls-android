plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(libs.sage.common.coroutines)
}

android {
    namespace = "com.vgleadsheets.coroutines"
}
