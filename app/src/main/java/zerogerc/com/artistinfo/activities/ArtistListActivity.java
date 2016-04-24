package zerogerc.com.artistinfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import zerogerc.com.artistinfo.R;
import zerogerc.com.artistinfo.database.ArtistReaderContract;
import zerogerc.com.artistinfo.loaders.ArtistsLoadTask;
import zerogerc.com.artistinfo.loaders.NetworkArtistLoadTask;

/**
 * Main Activity for showing all artists on the {@link android.support.v7.widget.RecyclerView}.
 */
public class ArtistListActivity extends RecyclerViewArtistsActivity {
    @Override
    protected int getContentId() {
        return R.layout.activity_artist_list;
    }

    @Override
    protected int getRecyclerId() {
        return R.id.artists_recycler_view;
    }

    /**
     * Creates task that load data from internet.
     * Additionally it shows refresh button if error during load occurred.
     * @return created task
     */
    @Override
    protected ArtistsLoadTask createArtistLoadTask() {
        return new NetworkArtistLoadTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                View v = findViewById(R.id.artists_recycler_view);
                if (v != null) {
                    v.bringToFront();
                }
                v = findViewById(R.id.refresh_layout);
                if (v != null) {
                    v.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (!aBoolean) {
                    View v = findViewById(R.id.refresh_layout);
                    if (v != null) {
                        v.setVisibility(View.VISIBLE);
                        v.bringToFront();
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startLoad();
                            }
                        });
                    }
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            startLoad();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_favourites || id == R.id.action_recent) {
            Intent intent = new Intent(this, DatabaseRequestActivity.class);
            if (id == R.id.action_favourites) {
                intent.putExtra(DatabaseRequestActivity.REQUEST_KEY, ArtistReaderContract.REQUEST_TYPE_FAVOURITES);
            } else {
                intent.putExtra(DatabaseRequestActivity.REQUEST_KEY, ArtistReaderContract.REQUEST_TYPE_RECENT);
            }
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
