plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di.jvm)
}

dependencies {
    api(projects.vgls.common.analytics)
    implementation(projects.vgls.common.appcomm)
    api(libs.sage.common.images)
    api(libs.sage.common.list)
    api(projects.vgls.common.model)
    api(projects.vgls.common.nav)
    api(libs.sage.common.pdf)
    api(projects.vgls.common.repository)
    api(libs.sage.common.ui.components)
    api(projects.vgls.common.urlinfo)
    implementation(projects.vgls.common.strings)
}
