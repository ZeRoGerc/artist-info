package zerogerc.com.artistinfo.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.adapter.BaseAdapter;
import zerogerc.com.artistinfo.loaders.ArtistsLoadTask;

/**
 * Activity with <code>RecyclerView</code> on it. This <code>Activity</code> uses {@link ArtistsLoadTask} provided by
 * {@link #createArtistLoadTask()} to populate artist list.
 * Method {@link #setContentView(int)} will be invoked automatically with {@link #getContentId()} as a parameter.
 * This activity also provides ability to rotate.
 */
public abstract class RecyclerViewArtistsActivity extends AppCompatActivity {
    private static final String ARTISTS_LIST_KEY = "ARTISTS";
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_view_state";
    private static final int LANDSCAPE_COLUMNS_COUNT = 3;

    protected RecyclerView recyclerView;
    protected BaseAdapter<Artist> adapter;
    protected ArrayList<Artist> artistList;

    private ArtistsLoadTask loadTask;


    /**
     * Provide content of Activity. This class uses this method to call {@link #setContentView(int)}
     * @return content of the Activity
     */
    protected abstract int getContentId();

    /**
     * Provide resource of <code>RecyclerView</code> on the <code>Activity</code>.
     * @return resource of <code>RecyclerView</code>
     */
    protected abstract int getRecyclerId();

    /**
     * Create and provide proper artist load task. Don't set adapters to it. It will be done automatically.
     * @return proper load task
     */
    protected abstract ArtistsLoadTask createArtistLoadTask();

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentId());

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
    @CallSuper
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
    @CallSuper
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


    private void initRecyclerView(boolean loaded) {
        if (artistList == null) {
            artistList = new ArrayList<>();
        }

        recyclerView = ((RecyclerView) findViewById(getRecyclerId()));
        if (recyclerView == null) {
            return;
        }

        recyclerView.setHasFixedSize(true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(LANDSCAPE_COLUMNS_COUNT, StaggeredGridLayoutManager.VERTICAL));
        }
        adapter = new BaseAdapter<>(this, artistList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (loaded) {
            restoreLoadTask();
            if (loadTask != null) {
                loadTask.setAdapter(adapter);
            }
        }
    }

    /**
     * Metoh which starts to populate artist list.
     */
    public void startLoad() {
        loadTask = createArtistLoadTask();
        loadTask.setAdapter(adapter);
        loadTask.execute();
    }
}
