plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    api(libs.sage.common.appcomm)
    api(libs.sage.common.coroutines)
    api(libs.sage.common.logging)
    api(projects.vgls.common.notif)
    api(projects.vgls.common.repository)
    api(libs.sage.common.storage.common)
    api(libs.sage.common.ui.strings)

    implementation(projects.vgls.common.appcomm)
}
