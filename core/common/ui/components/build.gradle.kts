plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(libs.kotlinx.collections.immutable)

    api(projects.core.common.ui.icons)
}

