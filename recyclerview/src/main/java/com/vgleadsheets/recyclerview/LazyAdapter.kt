package com.vgleadsheets.recyclerview

import androidx.recyclerview.widget.RecyclerView

/**
 * Workaround for a MvRx quirk - when screen rotation occurs, `onStart()` ensures that an
 * invalidate() happens, but it does so by posting it - so it doesn't happen immediately.
 * The result is that RecyclerView will likely fail to persist its scroll position.
 *
 * Rather than having your Fragment create its own RecyclerView Adapter, use this Lazy
 * provider to create it and ensure that your RecyclerView gets it. You do however need
 * to supply a function that provides your RecyclerView. (Providing it directly as an argument
 * will likely fail as your fragment has a null reference to it at construction time.)
 */
class LazyAdapter(private val rvProvider: () -> RecyclerView) : Lazy<ComponentAdapter> {
    private lateinit var adapter: ComponentAdapter

    private var initialized = false

    override val value: ComponentAdapter
        get() {
            if (!initialized) {
                adapter = ComponentAdapter()
                val recycler = rvProvider()
                recycler.adapter = adapter
                initialized = true
            }

            return adapter
        }

    override fun isInitialized() = initialized
}