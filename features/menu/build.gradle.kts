plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di.jvm)
}

dependencies {
    implementation(projects.vgls.common.appcomm)
    api(libs.sage.common.appinfo)
    api(libs.sage.common.list)
    api(projects.vgls.common.model)
    api(projects.vgls.common.nav)
    api(projects.vgls.common.repository)
    api(projects.vgls.common.offline)
    api(libs.sage.common.settings.general)
    api(libs.sage.common.time)
    api(libs.sage.common.ui.components)
    api(projects.vgls.common.strings)
}
