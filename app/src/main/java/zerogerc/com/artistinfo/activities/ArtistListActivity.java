package zerogerc.com.artistinfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import zerogerc.com.artistinfo.R;
import zerogerc.com.artistinfo.database.ArtistReaderContract;
import zerogerc.com.artistinfo.loaders.ArtistsLoadTask;
import zerogerc.com.artistinfo.loaders.NetworkArtistLoadTask;

public class ArtistListActivity extends RecyclerViewArtistsActivity {
    @Override
    protected int getContentId() {
        return R.layout.activity_artist_list;
    }

    @Override
    protected int getRecyclerId() {
        return R.id.artists_recycler_view;
    }

    @Override
    protected ArtistsLoadTask createArtistLoadTask() {
        return new NetworkArtistLoadTask();
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
        //If now activity showing the list of favourites or recent we just switch to full artist list.
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

    /**
     * Switch list of artists.
     * @param type type of content. Can be {@link ArtistReaderContract#REQUEST_TYPE_FAVOURITES},
     * {@link ArtistReaderContract#REQUEST_TYPE_RECENT} or <code>null</code>. If param is null activity starts showing all artists.
     */
//    private void switchArtistsList(final String type) {
//        if (type != null && (type.equals(ArtistReaderContract.REQUEST_TYPE_FAVOURITES) || type.equals(ArtistReaderContract.REQUEST_TYPE_RECENT))) {
//            //Here we assume that activity can't make rotation during this task because it's very fast task
//            final DatabaseArtistLoadTask loader = new DatabaseArtistLoadTask(getApplicationContext(), type);
//
//            currentArtists = new ArrayList<>();
//            adapter.attachTolist(currentArtists);
//            loader.setAdapter(adapter);
//            loader.execute();
//        } else {
//            currentArtists = artistList;
//            adapter.attachTolist(currentArtists);
//        }
//        recyclerView.getAdapter().notifyDataSetChanged();
//    }
}
