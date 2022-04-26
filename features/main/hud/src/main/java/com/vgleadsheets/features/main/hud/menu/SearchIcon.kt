package com.vgleadsheets.features.main.hud.menu

import android.widget.ImageButton
import com.vgleadsheets.features.main.hud.R

object SearchIcon {
    fun ImageButton.setIcon(state: State) {
        when (state) {
            State.HAMBURGER -> showMenuButton()
            State.BACK -> showBackButton()
            State.CLOSE -> showCloseButton()
        }
    }

    private fun ImageButton.showMenuButton() {
        setIconAndContentDesc(R.drawable.ic_menu_24dp, R.string.cd_search_menu)
    }

    private fun ImageButton.showBackButton() {
        setIconAndContentDesc(R.drawable.ic_arrow_back_black_24dp, R.string.cd_search_back)
    }

    private fun ImageButton.showCloseButton() {
        setIconAndContentDesc(R.drawable.ic_clear_black_24dp, R.string.cd_search_close)
    }

    private fun ImageButton.setIconAndContentDesc(
        iconId: Int,
        contentDescId: Int
    ) {
        contentDescription = resources.getString(contentDescId)
        setImageResource(iconId)
    }

    enum class State {
        HAMBURGER,
        BACK,
        CLOSE
    }
}
