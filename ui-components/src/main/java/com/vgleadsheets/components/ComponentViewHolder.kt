package com.vgleadsheets.components

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class ComponentViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(modelToBind: ListModel) {
        binding.setVariable(BR.toBind, modelToBind)
        binding.executePendingBindings()
    }

    @Suppress("unused")
    fun bind(modelToBind: ListModel, payloads: MutableList<Any>?) {
        if (payloads != null) bind(modelToBind)
    }
}
