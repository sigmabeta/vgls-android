plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    api(libs.sage.common.images)
    api(libs.sage.common.list)
    api(projects.vgls.common.model)
    api(libs.sage.common.nav)
    api(libs.sage.common.pdf)
    api(projects.vgls.common.repository)
    api(libs.sage.common.ui.components)
    api(projects.vgls.common.urlinfo)
}
