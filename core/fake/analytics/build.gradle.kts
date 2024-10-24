plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    // Contains the Tracker interface
    api(projects.core.common.analytics)

    implementation(projects.core.common.coroutines)
    implementation(projects.core.common.logging)
}
