plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di.jvm)
}

dependencies {
    api(projects.vgls.common.analytics)
    implementation(projects.vgls.common.appcomm)
    implementation(libs.sage.common.list)
    implementation(projects.vgls.common.notif)
    implementation(projects.vgls.common.offline)
    implementation(libs.sage.common.pdf)
    implementation(projects.vgls.common.repository)
    implementation(libs.sage.common.time)
    implementation(libs.sage.common.ui.components)
    implementation(projects.vgls.common.strings)
    implementation(projects.vgls.common.nav)
}
