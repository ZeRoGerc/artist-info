package zerogerc.com.artistinfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.R;
import zerogerc.com.artistinfo.database.ArtistReaderContract;
import zerogerc.com.artistinfo.extra.Utils;

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

        initFab();
        initViewFromArtist();
    }

    private void initFab() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final ArtistReaderContract.ArtistReaderDbHelper helper = new ArtistReaderContract.ArtistReaderDbHelper(getApplicationContext());

        FABClickDelete = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setImageResource(R.drawable.ic_star_border_white_24dp);
                helper.deleteArtist(artist, ArtistReaderContract.REQUEST_TYPE_FAVOURITES);
                Snackbar.make(fab, R.string.fab_action_database_delete, Snackbar.LENGTH_SHORT).show();
                fab.setOnClickListener(FABClickInsert);
            }
        };

        FABClickInsert = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setImageResource(R.drawable.ic_star_white_24dp);
                helper.insertArtist(artist, ArtistReaderContract.REQUEST_TYPE_FAVOURITES);
                Snackbar.make(fab, R.string.fab_action_database_insert, Snackbar.LENGTH_SHORT).show();
                fab.setOnClickListener(FABClickDelete);
            }
        };

        if (helper.hasArtist(artist, ArtistReaderContract.REQUEST_TYPE_FAVOURITES)) {
            fab.setImageResource(R.drawable.ic_star_white_24dp);
            fab.setOnClickListener(FABClickDelete);
        } else {
            fab.setImageResource(R.drawable.ic_star_border_white_24dp);
            fab.setOnClickListener(FABClickInsert);
        }
    }

    private void initViewFromArtist() {
        ((TextView) findViewById(R.id.full_info_genres)).setText(TextUtils.join(", ", artist.getGenres().toArray()));

        ((TextView) findViewById(R.id.full_info_track_albums)).setText(Utils.getTrackAlbumsString(getApplicationContext(), artist));

        // Convert first letter of description to Upper Case (just for good look)
        String sString = artist.getDescription();
        ((TextView) findViewById(R.id.full_info_bio)).setText(Character.toString(sString.charAt(0)).toUpperCase() + sString.substring(1));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
