package com.vgleadsheets.games

import android.view.View
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.recyclerview.BaseArrayAdapter
import com.vgleadsheets.recyclerview.ListView

class GameListAdapter(view: ListView) : BaseArrayAdapter<Game, GameViewHolder>(view) {
    override fun createViewHolder(view: View, viewType: Int) = GameViewHolder(view, this)

    override fun bind(holder: GameViewHolder, item: Game) = holder.bind(item)

    override fun getLayoutId(item: Game?) = R.layout.list_item_game
}
