plugins {
    alias(libs.plugins.vgls.core.android)
}

android {
    namespace = "com.vgleadsheets.ui.strings"
}

dependencies {
    api(libs.sage.common.ui.strings)
}
