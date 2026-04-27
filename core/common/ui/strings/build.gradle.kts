plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    implementation(libs.moshi)
    implementation(projects.core.common.connectivity)
}
