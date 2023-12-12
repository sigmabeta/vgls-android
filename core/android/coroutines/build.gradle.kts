plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(projects.core.common.coroutines)
}

android {
    namespace = "com.vgleadsheets.coroutines"
}
