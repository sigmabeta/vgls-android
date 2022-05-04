package com.vgleadsheets.common.parts

enum class PartSelectorOption(
    val apiId: String,
    val resId: Int,
    val longResId: Int
) {
    C("C", R.string.part_c, R.string.part_long_c),
    B("Bb", R.string.part_b, R.string.part_long_b),
    E("Eb", R.string.part_e, R.string.part_long_e),
    F("F", R.string.part_f, R.string.part_long_f),
    ALTO("Alto", R.string.part_alto, R.string.part_long_alto),
    BASS("Bass", R.string.part_bass, R.string.part_long_bass),
    VOCAL("Vocals", R.string.part_vocal, R.string.part_long_vocal)
}
