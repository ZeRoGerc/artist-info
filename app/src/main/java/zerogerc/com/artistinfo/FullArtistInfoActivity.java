package zerogerc.com.artistinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import zerogerc.com.artistinfo.database.ArtistReaderContract;

public class FullArtistInfoActivity extends AppCompatActivity {
    public static final String ARTIST_KEY = "Artist";
    private Artist artist;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_artist_info);

        Intent intent = getIntent();
        artist = intent.getParcelableExtra(ARTIST_KEY);

        ArtistReaderContract.ArtistReaderDbHelper helper = new ArtistReaderContract.ArtistReaderDbHelper(getApplicationContext());
        if (artist.getName().equals("Usher")) {
            helper.insertArtistRecent(artist);
        } else {
            helper.insertArtistFavourites(artist);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(artist.getName());

        if (findViewById(R.id.full_info_toolbar_background) != null) {
            Picasso.with(this)
                    .load(artist.getBigPicAddress())
                    .fit()
                    .centerCrop()
                    .into(((ImageView) findViewById(R.id.full_info_toolbar_background)));
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });

        initViewFromArtist();
    }

    private void initViewFromArtist() {
        ((TextView) findViewById(R.id.full_info_genres)).setText(TextUtils.join(", ", artist.getGenres().toArray()));

        ((TextView) findViewById(R.id.full_info_track_albums)).setText(Utils.getTrackAlbumsString(getApplicationContext(), artist));

        // Convert first letter of description to Upper Case (just for good look)
        String sString = artist.getDescription();
        ((TextView) findViewById(R.id.full_info_bio)).setText(Character.toString(sString.charAt(0)).toUpperCase()+sString.substring(1));
    }
}
