package com.vgleadsheets.games

import android.view.View
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.recyclerview.BaseViewHolder
import kotlinx.android.synthetic.main.list_item_game.view.*

class GameViewHolder(view: View, adapter: GameListAdapter) :
    BaseViewHolder<GameEntity, GameViewHolder, GameListAdapter>(view, adapter) {
    override fun bind(toBind: GameEntity) {
        view.text_game_name.text = toBind.name
//        view.text_sheet_count.text = "${toBind.songs.size} sheets"
    }
}