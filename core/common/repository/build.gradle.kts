plugins {
    alias(libs.plugins.vgls.core.jvm)
}
dependencies {
    api(projects.core.common.model)

    implementation(projects.core.common.appcomm)
    implementation(projects.core.common.conversion)
    implementation(projects.core.common.database)
    implementation(projects.core.common.logging)
    implementation(projects.core.common.network)
    implementation(projects.core.common.notif)
    implementation(projects.core.common.ui.strings)
    implementation(projects.core.common.time)
    implementation(projects.core.common.tracking)
}
