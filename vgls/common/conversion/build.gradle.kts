plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    api(projects.vgls.common.model)
    implementation(libs.sage.common.coroutines)
    implementation(projects.vgls.common.network)
}
