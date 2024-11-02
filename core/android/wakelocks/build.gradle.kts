plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    implementation(libs.androidx.activity)

    api(projects.core.common.wakelocks)

    implementation(projects.core.common.appcomm)
    implementation(projects.core.common.coroutines)
    implementation(projects.core.common.ui.strings)
}

android {
    namespace = "com.vgleadsheets.wakelocks"
}
