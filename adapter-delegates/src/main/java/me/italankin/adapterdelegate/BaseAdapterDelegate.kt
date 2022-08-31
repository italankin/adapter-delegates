package me.italankin.adapterdelegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Base implementation of [AdapterDelegate].
 *
 * @param VH type of [RecyclerView.ViewHolder] for this delegate
 * @param T  item type, managed by this delegate
 */
abstract class BaseAdapterDelegate<VH : RecyclerView.ViewHolder, T> : AdapterDelegate<VH, T> {

    /**
     * @return adapter, which this delegate is attached to
     */
    protected lateinit var adapter: CompositeAdapter<T>

    /**
     * Get layout resource identifier for view holder's layout.
     *
     * @return resource identifier for view holder layout
     */
    @get:LayoutRes
    protected abstract val layoutRes: Int

    /**
     * Create [RecyclerView.ViewHolder].
     *
     * @param itemView layout, inflated from [.getLayoutRes]
     * @return new [RecyclerView.ViewHolder]
     */
    protected abstract fun createViewHolder(itemView: View): VH

    override fun onAttached(adapter: CompositeAdapter<T>) {
        this.adapter = adapter
    }

    override fun onCreate(inflater: LayoutInflater, parent: ViewGroup): VH {
        val view = inflater.inflate(layoutRes, parent, false)
        return createViewHolder(view)
    }

    override fun onBind(holder: VH, position: Int, item: T, payloads: List<*>) {
        onBind(holder, position, item)
    }

    override fun onRecycled(holder: VH) {
    }

    override fun onFailedToRecycle(holder: VH): Boolean {
        return false
    }

    override fun getItemId(position: Int, item: T): Long {
        return 0
    }

    fun getItem(position: Int): T {
        return adapter.getItem(position)
    }
}
