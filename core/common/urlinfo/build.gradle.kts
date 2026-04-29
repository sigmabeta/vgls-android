plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(libs.sage.common.coroutines)
    api(projects.core.common.settings.general)
    api(projects.core.common.settings.environment)
    api(projects.core.common.settings.part)
}
