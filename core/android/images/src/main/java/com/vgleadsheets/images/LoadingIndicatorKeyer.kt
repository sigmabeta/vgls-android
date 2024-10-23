package com.vgleadsheets.images

import coil3.key.Keyer
import coil3.request.Options

class LoadingIndicatorKeyer : Keyer<LoadingIndicatorConfig> {
    override fun key(data: LoadingIndicatorConfig, options: Options): String {
        val width = options.size.width
        return "loading-${data.title}-${data.gameName}-C-$width"
    }
}
