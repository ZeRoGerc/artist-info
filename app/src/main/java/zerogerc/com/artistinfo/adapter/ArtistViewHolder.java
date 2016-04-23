package zerogerc.com.artistinfo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.activities.FullArtistInfoActivity;
import zerogerc.com.artistinfo.R;
import zerogerc.com.artistinfo.extra.SquareImageView;
import zerogerc.com.artistinfo.extra.Utils;

/**
 * Created by ZeRoGerc on 11/04/16.
 */
public abstract class ArtistViewHolder extends BaseViewHolder<Artist> {
    protected SquareImageView artistImage;
    protected TextView artistName;
    protected TextView artistGenres;
    protected TextView artistTracks;
    protected Activity activity;


    public ArtistViewHolder(Activity activity, View itemView) {
        super(activity, itemView);

        this.activity = activity;
        artistName = ((TextView) itemView.findViewById(R.id.artist_name));
        artistGenres = ((TextView) itemView.findViewById(R.id.artist_genres));
        artistImage = ((SquareImageView) itemView.findViewById(R.id.artist_image));
        artistTracks = ((TextView) itemView.findViewById(R.id.artist_tracks));
    }

    @Override
    @CallSuper
    public void refresh(final Artist item) {
        if (artistName != null) {
            artistName.setText(item.getName());
        }
        if (artistGenres != null) {
            artistGenres.setText(TextUtils.join(", ", item.getGenres().toArray()));
        }
        if (artistTracks != null) {
            artistTracks.setText(Utils.getTrackAlbumsString(activity, item));
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FullArtistInfoActivity.class);
                intent.putExtra(FullArtistInfoActivity.ARTIST_KEY, item);
                activity.startActivity(intent);
            }
        });
    }
}
