package com.vgleadsheets.composables.subs

/**
 * Whether the platform should draw a real elevation shadow (`Modifier.shadow`) vs. fall back to a
 * plain clipped surface. On Android this gates on API level (shadows on rounded surfaces misbehaved
 * before Q); on the JVM/desktop shadows are always fine.
 */
internal expect fun platformSupportsElevationShadow(): Boolean
