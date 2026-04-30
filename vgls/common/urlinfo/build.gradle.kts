plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    api(libs.sage.common.coroutines)
    api(libs.sage.common.settings.general)
    api(libs.sage.common.settings.environment)
    api(projects.vgls.common.settings.part)
}
