plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    implementation(libs.sage.common.connectivity)
    implementation(libs.sage.common.logging)
    implementation(projects.core.common.network)
    implementation(libs.sage.common.pdf)
    implementation(projects.core.common.repository)
    implementation(projects.core.common.urlinfo)
}
