package com.vgleadsheets.common.debug

enum class GiantBombNetworkEndpoint(
    val displayStringId: Int,
    val url: String?
) {
    PROD(R.string.network_endpoint_prod, "https://www.giantbomb.com/api/"),
    MOCK(R.string.network_endpoint_mock, null),
}
