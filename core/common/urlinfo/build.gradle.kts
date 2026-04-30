plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(libs.sage.common.coroutines)
    api(libs.sage.common.settings.general)
    api(libs.sage.common.settings.environment)
    api(projects.core.common.settings.part)
}
