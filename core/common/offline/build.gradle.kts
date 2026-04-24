plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    implementation(projects.core.common.downloader)
    implementation(projects.core.common.logging)
    implementation(projects.core.common.repository)
    implementation(projects.core.common.time)
}
