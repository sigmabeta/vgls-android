package com.vgleadsheets.common.debug

enum class NetworkEndpoint(
    val displayStringId: Int,
    val url: String?
) {
    PROD(R.string.network_endpoint_prod, "https://www.vgleadsheets.com/"),
    SUPER(R.string.network_endpoint_super, "https://super.vgleadsheets.com/"),
    BETA(R.string.network_endpoint_beta, "https://beta.vgleadsheets.com/"),
    MOCK(R.string.network_endpoint_mock, null),
}
