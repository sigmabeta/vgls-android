plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(projects.core.common.model)
    implementation(projects.core.common.coroutines)
    implementation(projects.core.common.network)
}
