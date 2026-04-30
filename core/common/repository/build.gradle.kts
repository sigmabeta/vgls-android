plugins {
    alias(libs.plugins.vgls.core.jvm)
}
dependencies {
    api(projects.core.common.model)
    api(libs.sage.common.settings.general)

    implementation(libs.sage.common.appcomm)
    implementation(libs.sage.common.connectivity)
    implementation(projects.core.common.conversion)
    implementation(projects.core.common.database)
    implementation(libs.sage.common.logging)
    implementation(projects.core.common.network)
    implementation(projects.core.common.notif)
    implementation(libs.sage.common.time)
    implementation(libs.sage.common.analytics)
    implementation(libs.sage.common.ui.strings)
}
