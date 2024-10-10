import com.android.build.gradle.LibraryExtension
import com.vgleadsheets.plugins.components.configureAndroidCompose
import com.vgleadsheets.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class VglsFeatureAndroidComposeModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("vgleadsheets.feature.android")
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
                add("implementation", libs.findLibrary("kotlinx.collections.immutable").get())
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}
