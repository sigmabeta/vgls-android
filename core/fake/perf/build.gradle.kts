plugins {
    alias(libs.plugins.vgls.core.jvm)
}

dependencies { // Contains the PerfTracker interface
    api(projects.core.common.perf)
}
