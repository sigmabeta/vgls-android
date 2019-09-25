package com.vgleadsheets.components

interface ItemProvider {
    fun getItemAtPosition(position: Int): ListModel
}
