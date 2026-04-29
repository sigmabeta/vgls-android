plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(libs.kotlinx.collections.immutable)

    api(libs.sage.common.appcomm)
    api(projects.core.common.images)
    api(projects.core.common.pdf)
    api(libs.sage.common.ui.icons)
}
