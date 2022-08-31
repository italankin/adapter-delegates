package me.italankin.adapterdelegates.sample.delegate

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.italankin.adapterdelegate.BaseAdapterDelegate
import me.italankin.adapterdelegates.sample.R
import me.italankin.adapterdelegates.sample.item.Title

class TitleAdapterDelegate : BaseAdapterDelegate<TitleAdapterDelegate.ViewHolder, Title>() {

    override val layoutRes: Int = R.layout.item_title

    override fun isType(position: Int, item: Any): Boolean {
        return item is Title
    }

    override fun createViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun onBind(holder: ViewHolder, position: Int, item: Title) {
        holder.title.text = item.title
    }

    override fun getItemId(position: Int, item: Title): Long {
        return item.id
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
    }
}
