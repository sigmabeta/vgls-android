plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    api(projects.core.common.connectivity)
    implementation(projects.core.common.coroutines)
    implementation(libs.sage.common.logging)
}

android {
    namespace = "com.vgleadsheets.connectivity"
}
