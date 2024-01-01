plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    api(libs.androidx.lifecycle.viewModelCompose)
}

android {
    namespace = "com.vgleadsheets.viewmodel"
}
