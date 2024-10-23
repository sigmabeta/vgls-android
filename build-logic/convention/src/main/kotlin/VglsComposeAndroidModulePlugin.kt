import com.android.build.gradle.LibraryExtension
import com.vgleadsheets.plugins.components.configureAndroidCompose
import com.vgleadsheets.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class VglsComposeAndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            dependencies {
                add("implementation", libs.findLibrary("kotlinx.collections.immutable").get())
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}
