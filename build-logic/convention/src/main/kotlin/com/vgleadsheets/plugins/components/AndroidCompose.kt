/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vgleadsheets.plugins.components

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        configureKotlinCompose()

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()

            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))

            add("implementation", libs.findLibrary("androidx-compose-material3").get())
            add("implementation", libs.findLibrary("androidx-compose-foundation").get())
            add("implementation", libs.findLibrary("androidx-compose-runtime-tracing").get())
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())

            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }
}

private fun Project.configureKotlinCompose() {
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        stabilityConfigurationFile.set(rootProject.layout.projectDirectory.file("compose-stability.conf"))
        reportsDestination.set(project.layout.buildDirectory.dir("compose_compiler/reports"))
        metricsDestination.set(project.layout.buildDirectory.dir("compose_compiler/metrics"))
    }

    tasks.withType<Test> {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 3).coerceAtLeast(1)
    }
}
