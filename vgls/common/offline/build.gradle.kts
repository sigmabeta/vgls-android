plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    implementation(projects.vgls.common.downloader)
    implementation(libs.sage.common.logging)
    implementation(projects.vgls.common.repository)
    implementation(libs.sage.common.time)
}
