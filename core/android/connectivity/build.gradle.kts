plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    api(projects.core.common.connectivity)
    implementation(projects.core.common.coroutines)
    implementation(projects.core.common.logging)
}

android {
    namespace = "com.vgleadsheets.connectivity"
}
