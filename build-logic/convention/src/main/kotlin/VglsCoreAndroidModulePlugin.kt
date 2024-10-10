import com.android.build.gradle.LibraryExtension
import com.vgleadsheets.plugins.components.configureKotlinAndroid
import com.vgleadsheets.plugins.components.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class VglsCoreAndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("io.gitlab.arturbosch.detekt")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
            }

            dependencies {
                add("implementation", libs.findLibrary("kotlin.stdlib").get())

                add("implementation", project(":core:common:logging"))
            }
        }
    }
}
