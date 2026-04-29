plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(libs.sage.common.coroutines)
    implementation(projects.core.common.settings.general)
}
