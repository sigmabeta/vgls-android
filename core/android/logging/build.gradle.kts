plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    api(projects.core.common.logging)
}

android {
    namespace = "com.vgleadsheets.logging"
}
