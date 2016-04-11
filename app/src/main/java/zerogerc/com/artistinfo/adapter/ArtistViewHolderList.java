package zerogerc.com.artistinfo.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.R;

/**
 * Created by ZeRoGerc on 10/04/16.
 */
public class ArtistViewHolderList extends ArtistViewHolder {
    public ArtistViewHolderList(Activity activity, ViewGroup parent) {
        super(activity, LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_layout_list, parent, false));
    }

    @Override
    public void refresh(Artist item) {
        if (artistName != null) {
            artistName.setText(item.getName());
        }
        if (artistGenres != null) {
            artistGenres.setText(TextUtils.join(", ", item.getGenres().toArray()));
        }
        if (artistImage != null) {
            Picasso.with(activity)
                    .load(item.getSmallPicAddress())
                    .into(artistImage);
        }
    }
}
