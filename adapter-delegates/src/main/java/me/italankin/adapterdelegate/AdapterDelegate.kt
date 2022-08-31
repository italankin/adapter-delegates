package me.italankin.adapterdelegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Interface for adapter delegate.
 *
 * @param VH type of the [RecyclerView.ViewHolder] for this delegate
 * @param T  data type for this delegate
 */
interface AdapterDelegate<VH : RecyclerView.ViewHolder, T> {

    /**
     * Called when this delegate is attached to [adapter].
     *
     * @param adapter adapter
     */
    fun onAttached(adapter: CompositeAdapter<T>)

    /**
     * Create [RecyclerView.ViewHolder].
     *
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
    fun onCreate(inflater: LayoutInflater, parent: ViewGroup): VH

    /**
     * Bind [holder] to the [item].
     *
     * @param holder   view holder
     * @param position position of `item`
     * @param item     item
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    fun onBind(holder: VH, position: Int, item: T)

    /**
     * Bind [holder] to the [item].
     *
     * @param holder   view holder
     * @param position position of `item`
     * @param item     item
     * @param payloads optional payloads
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    fun onBind(holder: VH, position: Int, item: T, payloads: List<Any?>)

    /**
     * Corresponds to [RecyclerView.Adapter.onViewRecycled] event.
     *
     * @param holder view holder
     */
    fun onRecycled(holder: VH)

    /**
     * Corresponds to [RecyclerView.Adapter.onFailedToRecycleView] event.
     *
     * @param holder view holder
     * @return `true` if the [holder] should be recycled, `false` otherwise
     */
    fun onFailedToRecycle(holder: VH): Boolean

    /**
     * Check if [item] at [position] can be managed by this delegate.
     *
     * @param position position of [item] in the dataset
     * @param item     item
     * @return `true`, if [item] is type managed by this delegate, otherwise `false`
     */
    fun isType(position: Int, item: Any): Boolean

    /**
     * Get an unique item ID.
     *
     * @param position position of `item`
     * @param item     item
     * @return unique identifier of `item`
     * @see RecyclerView.Adapter.hasStableIds
     */
    fun getItemId(position: Int, item: T): Long
}
