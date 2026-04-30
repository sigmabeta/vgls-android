plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    api(libs.sage.common.appcomm)
    api(libs.sage.common.coroutines)
    api(libs.sage.common.logging)
    api(projects.core.common.notif)
    api(projects.core.common.repository)
    api(libs.sage.common.storage.common)
    api(libs.sage.common.ui.strings)
}
