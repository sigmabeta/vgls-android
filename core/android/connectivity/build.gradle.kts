plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    api(libs.sage.common.connectivity)
    implementation(libs.sage.common.coroutines)
    implementation(libs.sage.common.logging)
}

android {
    namespace = "com.vgleadsheets.connectivity"
}
