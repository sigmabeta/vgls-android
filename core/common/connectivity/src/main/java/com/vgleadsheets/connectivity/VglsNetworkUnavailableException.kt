package com.vgleadsheets.connectivity

import java.io.IOException

class VglsNetworkUnavailableException(
    val networkStatus: NetworkStatus,
    message: String,
) : IOException(message)
