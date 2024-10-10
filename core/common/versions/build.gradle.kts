plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(projects.core.common.appcomm)
    api(projects.core.common.coroutines)
    api(projects.core.common.logging)
    api(projects.core.common.notif)
    api(projects.core.common.repository)
    api(projects.core.common.storage.common)
    api(projects.core.common.ui.strings)
}
