android {
    buildFeatures {
        compose true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = compose_version
    }
}

dependencies {
    // Jetpack Compose
    def composeBom = platform ("androidx.compose.compose-bom.2023.06.00)
        implementation composeBom
        androidTestImplementation composeBom

        implementation "androidx.compose.material3.material3"
        implementation "androidx.compose.ui.ui-tooling-preview"
        debugImplementation "androidx.compose.ui.ui-tooling"
}
