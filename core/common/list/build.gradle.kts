plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(libs.kotlinx.collections.immutable)

    api(projects.core.common.appcomm)
    api(projects.core.common.coroutines)
    api(projects.core.common.logging)
    api(projects.core.common.nav)
    api(projects.core.common.ui.strings)

    // For ListModel
    implementation(projects.core.common.ui.components)
}
