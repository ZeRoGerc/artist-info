package zerogerc.com.artistinfo.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.R;
import zerogerc.com.artistinfo.SquareImageView;

/**
 * Created by ZeRoGerc on 11/04/16.
 */
public abstract class ArtistViewHolder extends BaseViewHolder<Artist> {
    protected SquareImageView artistImage;
    protected TextView artistName;
    protected TextView artistGenres;


    public ArtistViewHolder(Activity activity, View itemView) {
        super(activity, itemView);

        artistName = ((TextView) itemView.findViewById(R.id.artist_name));
        artistGenres = ((TextView) itemView.findViewById(R.id.artist_genres));
        artistImage = ((SquareImageView) itemView.findViewById(R.id.artist_image));
    }
}
