plugins {
    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.compose.android)
    alias(libs.plugins.sage.di.android)
}

dependencies {
    api(libs.androidx.lifecycle.viewModelCompose)

    api(libs.sage.common.analytics)
    api(libs.sage.common.coroutines)
    api(libs.sage.common.debug)
    api(libs.sage.common.list)
    api(libs.sage.common.nav)
    api(libs.sage.common.perf)
    api(projects.vgls.common.repository)
    api(projects.vgls.common.urlinfo)

    implementation(libs.sage.common.ui.components)
}

android {
    namespace = "com.vgleadsheets.viewmodel"
}
