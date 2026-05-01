plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di.jvm)
}

dependencies {
    implementation(libs.sage.common.connectivity)
    implementation(libs.sage.common.logging)
    implementation(projects.vgls.common.network)
    implementation(libs.sage.common.pdf)
    implementation(projects.vgls.common.repository)
    implementation(projects.vgls.common.urlinfo)
}
