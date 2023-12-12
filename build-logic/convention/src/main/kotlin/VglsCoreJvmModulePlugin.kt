import com.vgleadsheets.plugins.components.configureKotlinJvm
import com.vgleadsheets.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class VglsCoreJvmModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }

            configureKotlinJvm()

            dependencies {
                add("implementation", libs.findLibrary("kotlin.stdlib").get())
            }
        }
    }
}
