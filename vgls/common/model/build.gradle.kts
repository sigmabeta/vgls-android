plugins {
    alias(libs.plugins.sage.kmp)
}

kotlin {
    android {
        namespace = "com.vgleadsheets.model"
    }

    sourceSets {
        // Existing src/main/java code lands in jvmSharedMain (shared android+jvm) — it uses java.*,
        // so it can't be pure commonMain/JS yet; migrating specific files to commonMain is a later step.
        named("jvmSharedMain") {
            dependencies {
                // So we don"t have buggy time comparisons
                implementation(libs.threeten)
            }
        }
    }
}
