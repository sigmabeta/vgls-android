plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    // So we don"t have buggy time comparisons
    implementation(libs.threeten)
}
