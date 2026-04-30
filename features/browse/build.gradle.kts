plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    api(libs.sage.common.list)
    api(libs.sage.common.nav)
    api(projects.core.common.repository)
    api(libs.sage.common.ui.components)
}
