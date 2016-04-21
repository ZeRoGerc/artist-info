package zerogerc.com.artistinfo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.R;

/**
 * Created by ZeRoGerc on 10/04/16.
 */
public class ArtistViewHolderList extends ArtistViewHolder {
    public static String LOG_TAG = "ArtistViewHolderList";

    public ArtistViewHolderList(Activity activity, ViewGroup parent) {
        super(activity, LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_layout_list, parent, false));
    }

    @Override
    public void refresh(Artist item) {
        super.refresh(item);
        if (artistImage != null) {
            Picasso.with(activity)
                    .load(item.getSmallPicAddress())
                    .into(artistImage);
        }
    }
}
