package zerogerc.com.artistinfo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ZeRoGerc on 25.12.15.
 * ITMO University
 */


/**
 * Base class for adapters
 * No special actions
 * No animations
 *
 * @param <T> objects adapter is handling
 */
public class BaseAdapter<T extends Item> extends RecyclerView.Adapter<BaseViewHolder> {
    protected List<T> items;
    protected Activity activity;

    public BaseAdapter(Activity activity, List<T> items) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.getViewHolderByType(activity, parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.refresh(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
