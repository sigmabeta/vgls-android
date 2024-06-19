plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    implementation(projects.core.common.appcomm)
    implementation(projects.core.common.coroutines)
    implementation(projects.core.common.model)
    implementation(projects.core.common.storage.common)
}
