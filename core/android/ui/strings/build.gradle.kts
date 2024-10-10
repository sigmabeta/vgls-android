plugins {
    alias(libs.plugins.vgls.core.android)
}

android {
    namespace = "com.vgleadsheets.ui.strings"
}

dependencies {
    api(projects.core.common.ui.strings)
}
