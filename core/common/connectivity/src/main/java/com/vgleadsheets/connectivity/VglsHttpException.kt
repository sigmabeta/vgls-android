package com.vgleadsheets.connectivity

import java.io.IOException

class VglsHttpException(
    val code: Int,
    message: String,
) : IOException(message)
