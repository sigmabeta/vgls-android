import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class VglsFeatureAndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("vgleadsheets.core.android")
            }

            dependencies {
                add("implementation", project(":vgls:android:images"))
                add("implementation", project(":vgls:android:nav"))
                add("implementation", project(":vgls:android:ui:components"))
                add("implementation", project(":vgls:android:viewmodel"))

                add("implementation", "net.sigmabeta.sage:coroutines")
                add("implementation", "net.sigmabeta.sage:nav")
                add("implementation", project(":vgls:common:repository"))
            }
        }
    }
}
