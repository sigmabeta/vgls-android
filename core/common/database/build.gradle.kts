plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies {
    api(projects.core.common.coroutines)
    api(projects.core.common.model)
    implementation(project(":core:common:conversion"))
}
