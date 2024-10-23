package com.vgleadsheets.remaster

import coil3.ImageLoader
import com.vgleadsheets.versions.AppVersionManager
import javax.inject.Inject

class ActivityDependencyInitializer @Inject constructor(
    val imageLoader: ImageLoader,
    val appVersionManager: AppVersionManager,
)
