plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    implementation(projects.core.common.list)
    implementation(projects.core.common.notif)
    implementation(projects.core.common.ui.components)
}
