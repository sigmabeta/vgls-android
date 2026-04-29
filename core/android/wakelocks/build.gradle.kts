plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    implementation(libs.androidx.activity)

    api(libs.sage.common.wakelocks)

    implementation(libs.sage.common.appcomm)
    implementation(libs.sage.common.coroutines)
    implementation(libs.sage.common.ui.strings)
}

android {
    namespace = "com.vgleadsheets.wakelocks"
}
