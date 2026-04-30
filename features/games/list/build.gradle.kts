plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    api(libs.sage.common.list)
    api(projects.core.common.model)
    api(libs.sage.common.nav)
    api(projects.core.common.repository)
    api(libs.sage.common.ui.components)
}
