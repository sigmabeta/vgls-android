plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    // Contains the Tracker interface
    api(libs.sage.common.analytics)

    implementation(libs.sage.common.coroutines)
    implementation(libs.sage.common.logging)
}
