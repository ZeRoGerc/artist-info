package zerogerc.com.artistinfo.adapter;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Base class for ViewHolders BaseAdapter using
 *
 * Actions to implement new type of BaseViewHolder:
 *  1. Add case to makeViewHolder(), that return BaseViewHolder of new type
 *  2. Implement refresh()
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    protected Activity activity;

    public BaseViewHolder(Activity activity, View itemView) {
        super(itemView);
        this.activity = activity;
    }

    /**
     * Method which refresh layout based on given item
     * @param item type of object to load on layout
     */
    public abstract void refresh(final T item);

    public static BaseViewHolder getViewHolderByType(Activity activity, ViewGroup parent, int viewType) {
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return new ArtistViewHolderList(activity, parent);
        } else {
            return new ArtistViewHolderGrid(activity, parent);
        }
    }
}
