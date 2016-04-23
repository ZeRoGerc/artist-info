package zerogerc.com.artistinfo.loaders;

import android.os.AsyncTask;
import android.util.Log;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.adapter.BaseAdapter;

/**
 * Base task for loading artist. Provide artists one by one in <code>onProgressUpdate</code>
 * Returns <code>true</code> on successful load, false otherwise.
 */
public abstract class ArtistsLoadTask extends AsyncTask<Void, Artist, Boolean> {
    public static final  String LOG_TAG = "ArtistLoadTask";

    private BaseAdapter<? super Artist> adapter;

    public ArtistsLoadTask() {}

    protected abstract Boolean loadArtists();

    @Override
    protected Boolean doInBackground(Void... params) {
        return loadArtists();
    }

    @Override
    protected void onProgressUpdate(Artist... values) {
        super.onProgressUpdate(values);
        if (adapter != null) {
            adapter.append(values[0]);
        } else {
            //Just for debugging
            Log.e(LOG_TAG, "Null adapter");
        }
    }

    public void setAdapter(BaseAdapter<? super Artist> adapter) {
        this.adapter = adapter;
    }
}
