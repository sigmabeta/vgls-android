package com.vgleadsheets.images

import coil.map.Mapper
import coil.request.Options

class SheetPreviewMapper(): Mapper<PagePreview, String> {
    override fun map(data: PagePreview, options: Options) = data.title
}
