package zerogerc.com.artistinfo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.R;
import zerogerc.com.artistinfo.activities.FullArtistInfoActivity;
import zerogerc.com.artistinfo.extra.Utils;

/**
 * View holder for showing artist on the {@link RecyclerView}
 */
public class ArtistViewHolder extends RecyclerView.ViewHolder {
    private ImageView artistImage;
    private TextView artistName;
    private TextView artistGenres;
    private TextView artistTracks;
    private Activity activity;


    public ArtistViewHolder(Activity activity, ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_layout, parent, false));
        this.activity = activity;
        artistName = ((TextView) itemView.findViewById(R.id.artist_name));
        artistGenres = ((TextView) itemView.findViewById(R.id.artist_genres));
        artistImage = ((ImageView) itemView.findViewById(R.id.artist_image));
        artistTracks = ((TextView) itemView.findViewById(R.id.artist_tracks));
    }

    /**
     * Method loads content of given artist onto this {@link ArtistViewHolder}.
     * @param artist given artist
     */
    public void refresh(final Artist artist) {
        if (artistName != null) {
            artistName.setText(artist.getName());
        }
        if (artistGenres != null) {
            artistGenres.setText(TextUtils.join(", ", artist.getGenres().toArray()));
        }
        if (artistTracks != null) {
            artistTracks.setText(Utils.getTrackAlbumsString(activity, artist));
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FullArtistInfoActivity.class);
                intent.putExtra(FullArtistInfoActivity.ARTIST_KEY, artist);
                activity.startActivity(intent);
            }
        });

        if (artistImage != null) {
            Picasso.with(activity)
                    .load(artist.getSmallPicAddress())
                    .into(artistImage);
        }
    }
}
