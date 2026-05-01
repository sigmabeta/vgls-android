plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di.jvm)
}

dependencies {
    api(libs.sage.common.list)
    api(projects.vgls.common.model)
    api(libs.sage.common.nav)
    api(projects.vgls.common.settings.part)
    api(libs.sage.common.ui.components)
    api(libs.sage.common.ui.strings)
}
