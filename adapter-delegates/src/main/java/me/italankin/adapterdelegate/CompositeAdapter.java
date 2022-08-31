package me.italankin.adapterdelegate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter}, which manages {@link AdapterDelegate}s.
 *
 * @param <T> data type for this adapter
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class CompositeAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final LayoutInflater inflater;
    protected final SparseArrayCompat<AdapterDelegate> delegates;
    protected final Context context;

    @NonNull
    protected List<? extends T> dataset = new ArrayList<>();

    protected CompositeAdapter(Context context, SparseArrayCompat<AdapterDelegate> delegates, boolean hasStableIds) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.delegates = delegates;
        setHasStableIds(hasStableIds);
        for (int i = 0, s = delegates.size(); i < s; i++) {
            delegates.valueAt(i).onAttached(this);
        }
    }

    /**
     * Set dataset for this adapter.
     *
     * @param data new dataset
     */
    public void setDataset(@Nullable List<T> data) {
        dataset = data != null ? data : new ArrayList<>();
    }

    /**
     * Get item at {@code position}.
     *
     * @param position position of item
     * @return item at {@code position}
     */
    public T getItem(int position) {
        return dataset.get(position);
    }

    /**
     * Get dataset, provided by {@link #setDataset(List)}.
     *
     * @return dateset
     */
    @NonNull
    public List<? extends T> getDataset() {
        return dataset;
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getLayoutInflater() {
        return inflater;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegates.get(viewType).onCreate(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        getDelegate(position).onBind(holder, position, getItem(position));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        getDelegate(position).onBind(holder, position, getItem(position), payloads);
    }

    @Override
    public int getItemViewType(int position) {
        T item = getItem(position);
        for (int i = 0, s = delegates.size(); i < s; i++) {
            if (delegates.valueAt(i).isType(position, item)) {
                return delegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException("Cannot get type for item at pos=" + position + ", item=" + item.toString());
    }

    @Override
    public long getItemId(int position) {
        return getDelegate(position).getItemId(position, getItem(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        delegates.get(holder.getItemViewType()).onRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        return delegates.get(holder.getItemViewType()).onFailedToRecycle(holder);
    }

    /**
     * Get delegate for item at {@code position}.
     *
     * @param position position of item
     * @return delegate for view at {@code position}
     * @throws IllegalArgumentException if adapter was not found
     */
    @NonNull
    protected AdapterDelegate getDelegate(int position) {
        int viewType = getItemViewType(position);
        AdapterDelegate delegate = delegates.get(viewType);
        if (delegate == null) {
            throw new IllegalArgumentException("No AdapterDelegate found for item at pos=" + position + ", viewType=" +
                    viewType);
        }
        return delegate;
    }

    /**
     * Builder for {@link CompositeAdapter}
     *
     * @param <T> this builder type
     */
    public static class Builder<T> extends BaseBuilder<T, Builder<T>, CompositeAdapter<T>> {
        public Builder(Context context) {
            super(context);
        }

        @Override
        protected CompositeAdapter<T> createAdapter() {
            return new CompositeAdapter<T>(context, delegates, hasStableIds);
        }
    }

    /**
     * Base builder for {@link CompositeAdapter}'s builders
     *
     * @param <T> list data type
     * @param <B> builder type
     * @param <A> adapter type
     */
    public abstract static class BaseBuilder<T, B extends BaseBuilder<T, B, A>, A extends CompositeAdapter<T>> {
        protected final Context context;
        protected final SparseArrayCompat<AdapterDelegate> delegates = new SparseArrayCompat<>(2);
        protected boolean hasStableIds;
        protected List<? extends T> dataset;
        protected RecyclerView recyclerView;

        protected BaseBuilder(Context context) {
            this.context = context;
        }

        /**
         * Whatever adapter should {@link CompositeAdapter#setHasStableIds(boolean) have stable ids}.
         *
         * @return this builder
         */
        @NonNull
        public B setHasStableIds(boolean hasStableIds) {
            this.hasStableIds = hasStableIds;
            return (B) this;
        }

        /**
         * Add delegate for adapter to use.
         *
         * @param delegate delegate
         * @return this builder
         */
        @NonNull
        public B add(AdapterDelegate<?, ? extends T> delegate) {
            return add(delegates.size(), delegate);
        }

        /**
         * Add delegate with explicit {@code viewType} for adapter to use.
         *
         * @param viewType view type; should be unique
         * @param delegate delegate
         * @return this builder
         */
        @NonNull
        public B add(int viewType, AdapterDelegate<?, ? extends T> delegate) {
            if (delegate == null) {
                throw new NullPointerException("delegate == null");
            }
            if (delegates.indexOfKey(viewType) >= 0) {
                throw new IllegalArgumentException("An adapter for viewType=" + viewType + " already exists");
            }
            delegates.put(viewType, delegate);
            return (B) this;
        }

        /**
         * Provide initial dataset for adapter.
         *
         * @param dataset dataset
         * @return this builder
         */
        @NonNull
        public B dataset(List<? extends T> dataset) {
            this.dataset = dataset;
            return (B) this;
        }

        /**
         * Set adapter for a given {@code recyclerView}.
         *
         * @param recyclerView recycler view
         * @return this builder
         * @see RecyclerView#setAdapter(RecyclerView.Adapter)
         */
        @NonNull
        public B recyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return (B) this;
        }

        /**
         * Create instance of {@link CompositeAdapter}.
         *
         * @return newly created instance of {@link CompositeAdapter}
         */
        @NonNull
        public A create() {
            if (delegates.size() == 0) {
                throw new IllegalStateException("No AdapterDelegates added");
            }
            A adapter = createAdapter();
            if (dataset != null) {
                adapter.dataset = dataset;
            }
            if (recyclerView != null) {
                recyclerView.setAdapter(adapter);
            }
            return adapter;
        }

        @NonNull
        protected abstract A createAdapter();
    }
}
