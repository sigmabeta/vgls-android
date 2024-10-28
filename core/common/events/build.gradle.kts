plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(projects.core.common.analytics)
    api(projects.core.common.appcomm)
}
