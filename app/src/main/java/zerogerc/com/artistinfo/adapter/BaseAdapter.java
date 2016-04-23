package zerogerc.com.artistinfo.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import zerogerc.com.artistinfo.extra.Utils;

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

    private int lastPosition = -1;

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
        setItemsAppearingAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void append(T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    private void setItemsAppearingAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //TODO measuring
            int dist;
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                dist = Utils.dpToPx(activity, activity.getResources().getConfiguration().screenHeightDp);
            } else {
                dist = Utils.dpToPx(activity, activity.getResources().getConfiguration().screenWidthDp);
            }

            ObjectAnimator animator = ObjectAnimator.ofFloat(viewToAnimate, "translationY", dist, 0);
            double x = Math.random();
            if (x > 0.66) {
                animator.setDuration(300);
            } else if (x > 0.33) {
                animator.setDuration(400);
            } else {
                animator.setDuration(500);
            }
            animator.setInterpolator(new DecelerateInterpolator());
//            Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left);
//            viewToAnimate.startAnimation(animation);
            animator.start();
            lastPosition = position;
        }
    }

    public void attachTolist(final List<T> list) {
        this.items = list;
    }
}
