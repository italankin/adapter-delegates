# Adapter Delegates [![Release](https://jitpack.io/v/me.italankin/adapter-delegates.svg)](https://jitpack.io/#me.italankin/adapter-delegates)


An opinionated library for working with `RecyclerView`'s adapters.

## Usage

### Add dependency

Add [JitPack](https://jitpack.io/) repository:

```groovy
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add dependency in your module (e.g. `app/build.gradle`):

```groovy
dependencies {
    implementation 'me.italankin:adapter-delegates:1.0.0'
}
```

### Write some code

Base components of the library are [`AdapterDelegate`](adapter-delegates/src/main/java/me/italankin/adapterdelegate/AdapterDelegate.kt) and [`CompositeAdapter`](adapter-delegates/src/main/java/me/italankin/adapterdelegate/CompositeAdapter.java).

You can either implement `AdapterDelegate` directly or use [`BaseAdapterDelegate`](adapter-delegates/src/main/java/me/italankin/adapterdelegate/BaseAdapterDelegate.kt) as a base class for your delegates:

```kotlin
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
    }
}
```

Then create an instance of `CompositeAdapter` and set as adapter of `RecyclerView`:

```kotlin
val adapter = CompositeAdapter.Builder<Any>(this)
    .add(TitleAdapterDelegate())
    // add more adapters if you need
    // an optional viewType can be provided:
    .add(R.layout.item_title, TitleAdapterDelegate())
    .create()

// provide a dataset for adapter
adapter.setDataset(createDataset())

// finally, set RecyclerView adapter
recyclerView.adapter = adapter
```

You can use `CompositeAdapter.Builder` methods to set dataset and attach adapter to `RecyclerView`:

```kotlin
val adapter = CompositeAdapter.Builder<Any>(this)
    .add(TitleAdapterDelegate())
    .dataset(createDataset())
    .recyclerView(recyclerView)
    .create()
```

### Usage with `DiffUtil`

The library provides [`Diffable`](adapter-delegates/src/main/java/me/italankin/adapterdelegate/Diffable.kt) interface:

```kotlin
class Title(
    // provide unique ID for this item
    override val id: Long,
    val title: String
) : Diffable {

    override fun contentEquals(other: Diffable): Boolean {
        return other is Title && title == other.title
    }
}
```

```kotlin
val adapter = DifferCompositeAdapter.Builder<Diffable>(this)
    .add(TitleAdapterDelegate())
    .recyclerView(recyclerView)
    .setHasStableIds(true)
    .create()

// submit list to apply changes
adapter.submitDataset(createDataset())

// for any change in the dataset create a copy of it
// it is required by DiffUtil to work correctly
val newDataset = ArrayList(adapter.dataset)
newDataset.removeFirst()
adapter.submitDataset(newDataset)
```

## License

	MIT License

	Copyright (c) 2022 Igor Talankin

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
