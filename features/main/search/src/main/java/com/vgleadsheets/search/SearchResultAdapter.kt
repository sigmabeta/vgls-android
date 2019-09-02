package com.vgleadsheets.search

import android.view.View
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.recyclerview.BaseArrayAdapter

class SearchResultAdapter() : BaseArrayAdapter<SearchResult, SearchResultViewHolder>() {
    override fun createViewHolder(view: View, viewType: Int) = SearchResultViewHolder(view, this)

    override fun bind(holder: SearchResultViewHolder, item: SearchResult) = holder.bind(item)

    override fun getLayoutId(item: SearchResult?) = R.layout.list_item_search_result

    override fun onItemClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
