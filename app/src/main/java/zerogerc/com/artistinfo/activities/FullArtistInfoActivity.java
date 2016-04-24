package zerogerc.com.artistinfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.R;
import zerogerc.com.artistinfo.database.ArtistReaderContract;
import zerogerc.com.artistinfo.extra.Utils;


/**
 * Activity for showing all info about given artist.
 * It load artist from Intent key {@link #ARTIST_KEY}
 */
public class FullArtistInfoActivity extends AppCompatActivity {
    public static final String ARTIST_KEY = "Artist";

    private Artist artist;
    private View.OnClickListener FABClickInsert;
    private View.OnClickListener FABClickDelete;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_artist_info);

        Intent intent = getIntent();
        artist = intent.getParcelableExtra(ARTIST_KEY);

        //Update timespamp of artist
        ArtistReaderContract.ArtistReaderDbHelper helper = new ArtistReaderContract.ArtistReaderDbHelper(getApplicationContext());
        helper.updateArtists(artist, ArtistReaderContract.REQUEST_TYPE_RECENT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle(artist.getName());
        }

        ImageView image = ((ImageView) findViewById(R.id.full_info_toolbar_background));
        if (image != null) {
            Picasso.with(this)
                    .load(artist.getBigPicAddress())
                    .fit()
                    .centerCrop()
                    .into(image);
        }

        initFab();
        initViewFromArtist();
    }

    /**
     * Initialize fab with listener based on database.
     */
    private void initFab() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final ArtistReaderContract.ArtistReaderDbHelper helper = new ArtistReaderContract.ArtistReaderDbHelper(getApplicationContext());

        FABClickDelete = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.deleteArtist(artist, ArtistReaderContract.REQUEST_TYPE_FAVOURITES);
                if (fab != null) {
                    fab.setImageResource(R.drawable.ic_star_border_white_24dp);
                    Snackbar.make(fab, R.string.fab_action_database_delete, Snackbar.LENGTH_SHORT).show();
                    fab.setOnClickListener(FABClickInsert);
                }
            }
        };

        FABClickInsert = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.updateArtists(artist, ArtistReaderContract.REQUEST_TYPE_FAVOURITES);
                if (fab != null) {
                    fab.setImageResource(R.drawable.ic_star_white_24dp);
                    Snackbar.make(fab, R.string.fab_action_database_insert, Snackbar.LENGTH_SHORT).show();
                    fab.setOnClickListener(FABClickDelete);
                }
            }
        };

        if (helper.hasArtist(artist, ArtistReaderContract.REQUEST_TYPE_FAVOURITES)) {
            if (fab != null) {
                fab.setImageResource(R.drawable.ic_star_white_24dp);
                fab.setOnClickListener(FABClickDelete);
            }
        } else {
            if (fab != null) {
                fab.setImageResource(R.drawable.ic_star_border_white_24dp);
                fab.setOnClickListener(FABClickInsert);
            }
        }
    }

    /**
     * Initialize all text information about artist
     */
    private void initViewFromArtist() {
        TextView genres = ((TextView) findViewById(R.id.full_info_genres));
        if (genres != null) {
            genres.setText(Utils.getGenresString(artist));
        }

        TextView track = ((TextView) findViewById(R.id.full_info_track_albums));
        if (track != null) {
            track.setText(Utils.getTrackAlbumsString(getApplicationContext(), artist));
        }

        TextView bio = ((TextView) findViewById(R.id.full_info_bio));
        if (bio != null) {
            bio.setText(Utils.firstToUpperCase(artist.getDescription()));
        }

        TextView link = ((TextView) findViewById(R.id.full_info_link));
        if (link != null) {
            link.setText(artist.getLink());
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FullArtistInfoActivity.this, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.URL_KEY, artist.getLink());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
