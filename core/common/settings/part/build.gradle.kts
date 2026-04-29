plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(libs.sage.common.coroutines)
    api(projects.core.common.model)
    api(projects.core.common.storage.common)
}
