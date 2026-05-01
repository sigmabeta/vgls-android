plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di.jvm)
}

dependencies {
    api(libs.sage.common.list)
    api(libs.sage.common.nav)
    api(projects.vgls.common.repository)
    api(libs.sage.common.ui.components)
}
