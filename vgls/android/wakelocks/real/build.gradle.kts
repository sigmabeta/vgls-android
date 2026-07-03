plugins {
    alias(libs.plugins.sage.android)
}

android {
    namespace = "com.vgleadsheets.wakelocks.real"
}

dependencies {
    api(projects.vgls.common.wakelocks.api)
    implementation(libs.androidx.activity)
    implementation(projects.vgls.common.strings)
    implementation(libs.sage.common.appcomm)
    implementation(libs.sage.common.coroutines)
    implementation(libs.sage.common.ui.strings)
}
