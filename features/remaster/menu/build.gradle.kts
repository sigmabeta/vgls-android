plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    api(projects.core.common.appinfo)
    api(projects.core.common.list)
    api(projects.core.common.model)
    api(projects.core.common.nav)
    api(projects.core.common.settings.general)
    api(projects.core.common.time)
    api(projects.core.common.ui.components)
    api(projects.core.common.ui.strings)
}
