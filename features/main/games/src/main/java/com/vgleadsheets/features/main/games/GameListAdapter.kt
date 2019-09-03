package com.vgleadsheets.features.main.games

import android.view.View
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.recyclerview.BaseArrayAdapter

class GameListAdapter(val view: GameListFragment) : BaseArrayAdapter<Game, GameViewHolder>() {
    override fun createViewHolder(view: View, viewType: Int) = GameViewHolder(view, this)

    override fun bind(holder: GameViewHolder, item: Game) = holder.bind(item)

    override fun getLayoutId(item: Game?) = R.layout.list_item_game

    override fun onItemClick(position: Int) = view.onItemClick(dataset!![position].id)
}
