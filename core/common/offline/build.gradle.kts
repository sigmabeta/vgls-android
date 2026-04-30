plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    implementation(projects.core.common.downloader)
    implementation(libs.sage.common.logging)
    implementation(projects.core.common.repository)
    implementation(libs.sage.common.time)
}
