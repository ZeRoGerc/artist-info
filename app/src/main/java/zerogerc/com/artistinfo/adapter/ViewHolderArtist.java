package zerogerc.com.artistinfo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.R;

/**
 * Created by ZeRoGerc on 10/04/16.
 */
public class ViewHolderArtist extends BaseViewHolder<Artist> {
    private ImageView artistImage;
    private TextView artistName;

    public ViewHolderArtist(Activity activity, ViewGroup parent) {
        super(activity, LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_artist_layout, parent, false));

        artistImage = ((ImageView) itemView.findViewById(R.id.artist_image));
        artistName = ((TextView) itemView.findViewById(R.id.artist_name));
}

    @Override
    public void refresh(Artist item) {
        Picasso.with(activity).load(item.getSmallPicAddress()).fit().centerCrop().into(artistImage);
        artistName.setText(item.getName());
    }
}
