package com.vgleadsheets.components

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class ComponentViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(modelToBind: Any) {
        binding.setVariable(BR.toBind, modelToBind)
        binding.executePendingBindings()
    }
}
