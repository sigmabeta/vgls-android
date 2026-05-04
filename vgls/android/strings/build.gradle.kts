plugins {
    alias(libs.plugins.sage.android)
}

android {
    namespace = "com.vgleadsheets.strings"
}

dependencies {
    api(projects.vgls.common.strings)
}
