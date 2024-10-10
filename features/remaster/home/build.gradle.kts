plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    implementation(projects.core.common.list)
    implementation(projects.core.common.notif)
    implementation(projects.core.common.pdf)
    implementation(projects.core.common.repository)
    implementation(projects.core.common.time)
    implementation(projects.core.common.ui.components)
}
