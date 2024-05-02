import com.vgleadsheets.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class VglsDiJvmModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-kapt")
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt-core").get())

                "kapt"(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}
