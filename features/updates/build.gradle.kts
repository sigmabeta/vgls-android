plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di)
}

dependencies {
    api(projects.vgls.common.analytics)
    api(libs.sage.common.appinfo)
    api(libs.sage.common.list)
    api(projects.vgls.common.model)
    api(projects.vgls.common.nav)
    api(projects.vgls.common.repository)
    api(libs.sage.common.ui.components)
    api(projects.vgls.common.strings)
    api(libs.sage.common.time)
    api(projects.vgls.common.viewmodel)
    implementation(libs.metrox.viewmodel)
}
