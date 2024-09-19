plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    implementation(projects.core.common.logging)
    implementation(projects.core.common.network)
    implementation(projects.core.common.pdf)
    implementation(projects.core.common.repository)
    implementation(projects.core.common.urlinfo)
}
