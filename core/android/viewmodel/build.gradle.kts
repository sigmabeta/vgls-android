plugins {
    alias(libs.plugins.vgls.core.android)
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.di.android)
}

dependencies {
    api(libs.androidx.lifecycle.viewModelCompose)

    api(libs.sage.common.analytics)
    api(libs.sage.common.coroutines)
    api(projects.core.common.debug)
    api(projects.core.common.list)
    api(libs.sage.common.nav)
    api(projects.core.common.perf)
    api(projects.core.common.repository)
    api(projects.core.common.urlinfo)

    implementation(projects.core.common.ui.components)
}

android {
    namespace = "com.vgleadsheets.viewmodel"
}
