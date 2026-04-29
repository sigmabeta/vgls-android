plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    api(projects.core.common.list)
    api(projects.core.common.model)
    api(libs.sage.common.nav)
    api(projects.core.common.repository)
    api(libs.sage.common.time)
    api(projects.core.common.ui.components)
    api(libs.sage.common.ui.strings)
}
