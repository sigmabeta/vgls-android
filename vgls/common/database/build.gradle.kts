plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    api(libs.sage.common.coroutines)
    api(projects.vgls.common.model)
}
