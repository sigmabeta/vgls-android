plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    api(libs.sage.common.ui.strings)
    api(libs.sage.common.connectivity)
    implementation(libs.moshi)
}
