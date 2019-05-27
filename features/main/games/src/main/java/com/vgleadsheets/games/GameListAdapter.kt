package com.vgleadsheets.games

import android.view.View
import com.vgleadsheets.games.R
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.recyclerview.BaseArrayAdapter
import com.vgleadsheets.recyclerview.ListView

class GameListAdapter(view: ListView) : BaseArrayAdapter<GameEntity, GameViewHolder>(view) {
    override fun createViewHolder(view: View, viewType: Int) = GameViewHolder(view, this)

    override fun bind(holder: GameViewHolder, item: GameEntity) = holder.bind(item)

    override fun getLayoutId(item: GameEntity?) = R.layout.list_item_game
}
