plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    // So we don"t have buggy time comparisons
    implementation(libs.threeten)
}
