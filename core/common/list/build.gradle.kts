plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(libs.kotlinx.collections.immutable)

    api(libs.sage.common.appcomm)
    api(projects.core.common.analytics)
    api(libs.sage.common.coroutines)
    api(libs.sage.common.logging)
    api(libs.sage.common.nav)
    api(projects.core.common.ui.strings)

    // For ListModel
    implementation(projects.core.common.ui.components)
}
