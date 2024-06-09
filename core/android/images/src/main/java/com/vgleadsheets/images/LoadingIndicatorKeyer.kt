package com.vgleadsheets.images

import coil.key.Keyer
import coil.request.Options
import javax.inject.Inject

class LoadingIndicatorKeyer @Inject constructor() : Keyer<LoadingIndicatorConfig> {
    override fun key(data: LoadingIndicatorConfig, options: Options): String {
        val width = options.size.width
        return "loading-${data.title}-${data.gameName}-${data.transposition}-$width"
    }
}
