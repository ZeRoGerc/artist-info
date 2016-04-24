package zerogerc.com.artistinfo.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.extra.Utils;


/**
 * Adaptor for {@link ArtistViewHolder}.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistViewHolder> {
    protected List<Artist> items;
    protected Activity activity;

    //For animations
    private int lastPosition = -1;

    public ArtistAdapter(Activity activity, List<Artist> items) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        //Always return same value because of single type
        return 0;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //We show only one type of content so just return this view holder
        return new ArtistViewHolder(activity, parent);
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        holder.refresh(items.get(position));
        setItemsAppearingAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Append given artist in the end of the list.
     * Changes will be shown on UI.
     * @param artist given artist
     */
    public void append(Artist artist) {
        items.add(artist);
        notifyItemInserted(items.size() - 1);
    }


    /**
     * Attach this adapter to given list.
     * @param list given list
     */
    public void attachTolist(final List<Artist> list) {
        this.items = list;
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
            animator.start();
            lastPosition = position;
        }
    }
}
