plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(projects.core.common.analytics)
    api(libs.sage.common.appcomm)
}
