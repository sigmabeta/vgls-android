plugins {
    alias(libs.plugins.sage.jvm)
    alias(libs.plugins.sage.di)
}

dependencies {
    // Multiplatform ViewModel base (androidx.lifecycle:lifecycle-viewmodel publishes commonMain
    // ViewModel + viewModelScope for both Android and JVM). api so feature VMs see the ViewModel type.
    api(libs.androidx.lifecycle.viewmodel)

    // ListState / ListStateActual / SageScheduler / DelayManager, plus (transitively, api) StringProvider,
    // appcomm (SageAction/SageEvent/EventDispatcher), analytics, coroutines, logging.
    api(libs.sage.common.list)
    api(libs.sage.common.debug)
    // TitleBarModel (ListState.title(...)) lives here; sage.common.list only `implementation`s it.
    implementation(libs.sage.common.ui.components)
}
