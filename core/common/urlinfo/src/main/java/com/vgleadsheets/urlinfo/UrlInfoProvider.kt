package com.vgleadsheets.urlinfo

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.settings.environment.EnvironmentManager
import com.vgleadsheets.settings.part.SelectedPartManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

class UrlInfoProvider(
    private val environmentManager: EnvironmentManager,
    private val partManager: SelectedPartManager,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
) {
    private val _urlInfoFlow = MutableStateFlow(UrlInfo())
    val urlInfoFlow = _urlInfoFlow.asStateFlow()

    init {
        combine(
            environmentManager.selectedEnvironmentFlow(),
            partManager.selectedPartFlow()
        ) { env, part ->
            _urlInfoFlow.update {
                UrlInfo(
                    baseBaseUrl = env.url,
                    apiBaseUrl = prependIfNotNull(env.url, "api/app/"),
                    imageBaseUrl = prependIfNotNull(env.url, "assets/sheets/png/"),
                    pdfBaseUrl = prependIfNotNull(env.url, "assets/sheets/"),
                    partId = part.apiId
                )
            }
        }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun prependIfNotNull(prefix: String?, suffix: String): String? {
        prefix ?: return null

        return prefix + suffix
    }
}
