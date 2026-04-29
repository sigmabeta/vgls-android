plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    implementation(libs.androidx.activity)

    api(libs.sage.common.wakelocks)

    implementation(projects.core.common.appcomm)
    implementation(libs.sage.common.coroutines)
    implementation(projects.core.common.ui.strings)
}

android {
    namespace = "com.vgleadsheets.wakelocks"
}
