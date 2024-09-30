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
                add("implementation", project(":core:android:images"))
                add("implementation", project(":core:android:nav"))
                add("implementation", project(":core:android:ui:components"))
                add("implementation", project(":core:android:viewmodel"))

                add("implementation", project(":core:common:coroutines"))
                add("implementation", project(":core:common:nav"))
                add("implementation", project(":core:common:repository"))
            }
        }
    }
}
