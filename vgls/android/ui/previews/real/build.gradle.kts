plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)

    alias(libs.plugins.sage.android)
    alias(libs.plugins.sage.compose.android)
}

dependencies {
    implementation(projects.features.all.di)
    implementation(projects.features.navbar.real)
    implementation(projects.features.topbar.real)

    implementation(projects.vgls.common.model.api)
    implementation(libs.kotlinx.datetime)
    implementation(libs.sage.common.ui.components)

    implementation(projects.vgls.android.bitmaps.real)
    implementation(projects.vgls.android.images.real)
    implementation(projects.vgls.android.pdf.real)
    implementation(libs.sage.common.ui.perfCompose)
    implementation(projects.vgls.android.scaffold.real)
    implementation(projects.vgls.android.ui.components.api)
    implementation(libs.sage.common.ui.iconsReal)
    implementation(libs.sage.common.ui.listScreens)
    implementation(projects.vgls.common.strings.api)
    implementation(libs.sage.android.ui.strings)
    implementation(projects.vgls.android.ui.theme.api)

    // CMP packages androidMain composeResources as Android assets only — off the JVM unit-test
    // classpath — so under Paparazzi the ClasspathResourceReader can't find the string/font `.cvr`
    // and text renders resource keys in the default face. Each resource module exposes its
    // composeResources as a classpath-shaped jar (composeResourcesElements); pull them as test deps so
    // the strings + MuseJazz font land on the Paparazzi classpath.
    listOf(":vgls:common:strings:api", ":vgls:android:ui:fonts:real").forEach { modulePath ->
        testImplementation(
            project(mapOf("path" to modulePath, "configuration" to "composeResourcesElements")),
        )
    }
}

android {
    namespace = "com.vgleadsheets.ui.previews"
}
