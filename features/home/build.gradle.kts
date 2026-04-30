plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    implementation(libs.sage.common.list)
    implementation(projects.core.common.notif)
    implementation(projects.core.common.offline)
    implementation(libs.sage.common.pdf)
    implementation(projects.core.common.repository)
    implementation(libs.sage.common.time)
    implementation(libs.sage.common.ui.components)
}
