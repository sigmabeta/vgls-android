package com.vgleadsheets.common.parts

enum class PartSelectorOption(
    val apiId: String,
    val resId: Int
) {
    C("C", R.string.part_c),
    B("Bb", R.string.part_b),
    E("Eb", R.string.part_e),
    F("F", R.string.part_f),
    ALTO("Alto", R.string.part_alto),
    BASS("Bass", R.string.part_bass),
    VOCAL("Vocals", R.string.part_vocal)
}
