plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(libs.sage.common.appcomm)
    api(libs.sage.common.coroutines)
    api(libs.sage.common.logging)
    api(projects.core.common.notif)
    api(projects.core.common.repository)
    api(libs.sage.common.storage.common)
    api(projects.core.common.ui.strings)
}
