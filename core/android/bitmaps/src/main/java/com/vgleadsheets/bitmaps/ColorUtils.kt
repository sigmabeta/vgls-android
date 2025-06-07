package com.vgleadsheets.bitmaps

import kotlin.random.Random

@Suppress("MagicNumber")
fun Random.nextColor(): Int {
    val alpha = 255
    val red = nextInt(255)
    val green = nextInt(255)
    val blue = nextInt(255)

    return (alpha and 0xff) shl 24 or ((red and 0xff) shl 16) or ((green and 0xff) shl 8) or (blue and 0xff)
}
