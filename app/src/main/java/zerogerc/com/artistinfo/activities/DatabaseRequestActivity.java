package zerogerc.com.artistinfo.activities;

import android.support.v7.app.ActionBar;
import android.os.Bundle;

import zerogerc.com.artistinfo.R;
import zerogerc.com.artistinfo.database.ArtistReaderContract;
import zerogerc.com.artistinfo.loaders.ArtistsLoadTask;
import zerogerc.com.artistinfo.loaders.DatabaseArtistLoadTask;


/**
 * Activity which load data from database and show it on the {@link android.support.v7.widget.RecyclerView}.
 * It determine the request type(favourites, recent...) from Intent key {@link #REQUEST_KEY}.
 */
public class DatabaseRequestActivity extends RecyclerViewArtistsActivity {
    public static final String REQUEST_KEY = "request";
    String requestType;

    @Override
    protected int getContentId() {
        return R.layout.activity_database_request;
    }

    @Override
    protected int getRecyclerId() {
        return R.id.artists_recycler_view;
    }

    @Override
    protected ArtistsLoadTask createArtistLoadTask() {
        return new DatabaseArtistLoadTask(getApplicationContext(), requestType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestType = getIntent().getStringExtra(REQUEST_KEY);
        super.onCreate(savedInstanceState);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            if (requestType.equals(ArtistReaderContract.REQUEST_TYPE_FAVOURITES)) {
                getSupportActionBar().setTitle(R.string.activity_favourites_title);
            } else {
                getSupportActionBar().setTitle(R.string.activity_recent_title);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        We need to track changes after FullArtistInfoActivity.
        For example user can remove artist from favourites and we need to track such changes for consistency.
        */
        artistList.clear();
        adapter.notifyDataSetChanged();
        startLoad();
    }
}
