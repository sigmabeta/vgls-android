plugins {
    alias(libs.plugins.vgls.core.jvm)
}
dependencies {
    // So we don"t have buggy time comparisons
    api(libs.threeten)

    api(projects.core.common.model)

    implementation(projects.core.common.appcomm)
    implementation(projects.core.common.conversion)
    implementation(projects.core.common.database)
    implementation(projects.core.common.logging)
    implementation(projects.core.common.network)
    implementation(projects.core.common.tracking)
}
