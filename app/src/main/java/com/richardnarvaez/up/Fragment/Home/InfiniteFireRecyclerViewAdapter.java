package com.richardnarvaez.up.Fragment.Home;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.richardnarvaez.up.Adapter.Firebase.InfiniteFireArray;

/**
 * Created by macbookpro on 4/30/18.
 */


public abstract class InfiniteFireRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected InfiniteFireArray<T> snapshots;
    protected int indexOffset;
    protected int indexAppendix;
    private String TAG = "Infinite";

    /**
     * Use this constructor if you got either multiple InfiniteFireArrays that you want to merge into one RecyclerView or if you want to add custom headers or footers.
     * This constructor does not add Listeners to anything, so you have to do that yourself.
     *
     * @param numHeaders Number of headers this RecyclerView adds to its length.
     * @param numFooters Number of footers this RecyclerView adds to its length.
     */
    public InfiniteFireRecyclerViewAdapter(
            final int numHeaders,
            final int numFooters
    ) {
        this.indexOffset = numHeaders;
        this.indexAppendix = numFooters;
    }


    public InfiniteFireRecyclerViewAdapter(
            InfiniteFireArray<T> snapshots,
            final int numHeaders,
            final int numFooters) {
        this.snapshots = snapshots;
        this.indexOffset = numHeaders;
        this.indexAppendix = numFooters;

        this.snapshots.setOnChangedListener(new InfiniteFireArray.OnChangedListener() {
            @Override
            public void onChanged(InfiniteFireArray.OnChangedListener.EventType type, int index, int oldIndex) {
                switch (type) {
                    case Added:
                        Log.e(TAG, "ADD: " + index + indexOffset);
                        notifyItemInserted(index + indexOffset);
                        break;
                    case Changed:
                        notifyItemChanged(index + indexOffset);
                        break;
                    case Removed:
                        notifyItemRemoved(index + indexOffset);
                        break;
                    case Moved:
                        notifyItemMoved(oldIndex + indexOffset, index + indexOffset);
                        break;
                    case Reset:
                        notifyDataSetChanged();
                        break;
                    default:
                        throw new IllegalStateException("Incomplete case statement");
                }
            }
        });
    }

    /**
     * Removes the Listener.
     */
    public void clear() {
        snapshots.setOnChangedListener(null);
        snapshots = null;
    }

    /**
     * @return Get the item count including headers and footers.
     */
    @Override
    public int getItemCount() {
        return snapshots.getCount() + indexOffset + indexAppendix;
    }

    /**
     * @param position Position of the item.
     * @return Position of the item because there are headers and footers for which we can't get a proper hash code.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
}
