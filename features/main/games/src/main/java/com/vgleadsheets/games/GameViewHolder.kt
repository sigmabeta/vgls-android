package com.vgleadsheets.games

import android.view.View
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_game.view.*

class GameViewHolder(view: View, adapter: GameListAdapter) :
    BaseViewHolder<Game, GameViewHolder, GameListAdapter>(view, adapter) {
    override fun bind(toBind: Game) {
        view.text_game_name.text = toBind.name
//        view.text_sheet_count.text = "${toBind.songs.size} sheets"
    }
}