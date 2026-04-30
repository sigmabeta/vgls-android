plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    api(libs.sage.common.appinfo)
    api(libs.sage.common.list)
    api(projects.core.common.model)
    api(libs.sage.common.nav)
    api(projects.core.common.repository)
    api(libs.sage.common.ui.components)
    api(libs.sage.common.ui.strings)
    api(libs.sage.common.time)
}
