package com.vgleadsheets.composables.subs

import android.os.Build

actual fun platformSupportsElevationShadow(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
