plugins {
    alias(libs.plugins.sage.jvm)
}

dependencies {
    api(projects.core.common.model)
    implementation(libs.sage.common.coroutines)
    implementation(projects.core.common.network)
}
