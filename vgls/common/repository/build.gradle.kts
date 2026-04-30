plugins {
    alias(libs.plugins.sage.jvm)
}
dependencies {
    api(projects.vgls.common.model)
    api(libs.sage.common.settings.general)

    implementation(libs.sage.common.appcomm)
    implementation(libs.sage.common.connectivity)
    implementation(projects.vgls.common.conversion)
    implementation(projects.vgls.common.database)
    implementation(libs.sage.common.logging)
    implementation(projects.vgls.common.network)
    implementation(projects.vgls.common.notif)
    implementation(libs.sage.common.time)
    implementation(libs.sage.common.analytics)
    implementation(libs.sage.common.ui.strings)
}
