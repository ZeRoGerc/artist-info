package zerogerc.com.artistinfo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import zerogerc.com.artistinfo.adapter.BaseAdapter;

public class ArtistListActivity extends AppCompatActivity {
    private static final String ARTISTS_LIST_KEY = "ARTISTS";
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_view_state";
    private static final int LANDSCAPE_COLUMNS_COUNT = 3;

    private RecyclerView recyclerView;
    private ArrayList<Artist> artistList;
    private ArtistsLoadTask loadTask;

    private void initRecyclerView(boolean loaded) {
        if (artistList == null) {
            artistList = new ArrayList<>();
        }

        recyclerView = ((RecyclerView) findViewById(R.id.artists_recycler_view));
        recyclerView.setHasFixedSize(true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(LANDSCAPE_COLUMNS_COUNT, StaggeredGridLayoutManager.VERTICAL));
        }
        BaseAdapter<Artist> adapter = new BaseAdapter<>(this, artistList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (loaded) {
            restoreLoadTask();
            if (loadTask != null) {
                loadTask.setAdapter(adapter);
            }
        } else {
            loadTask = new ArtistsLoadTask();
            loadTask.setAdapter(adapter);
            loadTask.execute();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if (loadTask != null) {
            return loadTask;
        }
        return super.onRetainCustomNonConfigurationInstance();
    }

    private void restoreLoadTask() {
        if (getLastCustomNonConfigurationInstance() != null) {
            loadTask = (ArtistsLoadTask) getLastCustomNonConfigurationInstance();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            initRecyclerView(false);
        } else {
            artistList = savedInstanceState.getParcelableArrayList(ARTISTS_LIST_KEY);
            initRecyclerView(true);

            int position = savedInstanceState.getInt(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().scrollToPosition(position);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (artistList != null) {
            outState.putParcelableArrayList(ARTISTS_LIST_KEY, artistList);
        }

        int position;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        } else {
            int[] positions = new int[LANDSCAPE_COLUMNS_COUNT];
            ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(positions);
            position = positions[0];
        }
        outState.putInt(BUNDLE_RECYCLER_LAYOUT, position);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favourites) {
        }

        if (id == R.id.action_recent) {
        }

        return super.onOptionsItemSelected(item);
    }
}
