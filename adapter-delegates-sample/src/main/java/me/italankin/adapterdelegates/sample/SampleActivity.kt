package me.italankin.adapterdelegates.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import me.italankin.adapterdelegate.Diffable
import me.italankin.adapterdelegate.DifferCompositeAdapter
import me.italankin.adapterdelegates.sample.delegate.TextAdapterDelegate
import me.italankin.adapterdelegates.sample.delegate.TitleAdapterDelegate
import me.italankin.adapterdelegates.sample.util.RandomDataset

class SampleActivity : AppCompatActivity(R.layout.activity_sample) {

    private lateinit var adapter: DifferCompositeAdapter<Diffable>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        adapter = DifferCompositeAdapter.Builder<Diffable>(this)
            .add(TitleAdapterDelegate())
            .add(TextAdapterDelegate { position, item ->
                val newDataset = ArrayList(adapter.dataset)
                newDataset.removeAt(position)
                adapter.submitDataset(newDataset)
            })
            .recyclerView(recyclerView)
            .setHasStableIds(true)
            .create()

        adapter.submitDataset(RandomDataset.get())
    }
}
