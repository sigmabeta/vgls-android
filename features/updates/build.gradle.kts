plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    api(libs.sage.common.appinfo)
    api(projects.core.common.list)
    api(projects.core.common.model)
    api(libs.sage.common.nav)
    api(projects.core.common.repository)
    api(projects.core.common.ui.components)
    api(projects.core.common.ui.strings)
    api(projects.core.common.time)
}
