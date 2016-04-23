package zerogerc.com.artistinfo.loaders;

import android.content.Context;
import android.database.Cursor;

import zerogerc.com.artistinfo.database.ArtistReaderContract;

public class DatabaseArtistLoadTask extends ArtistsLoadTask {
    Context context;
    String requestType;

    /**
     * It's very fast task so you could provide any <code>Context</code>. But the best choice is <code>ApplicationContext</code>.
     * @param context any context
     * @param  requestType type of request. Can be either {@link ArtistReaderContract#REQUEST_TYPE_FAVOURITES}
     *                     or {@link ArtistReaderContract#REQUEST_TYPE_RECENT}
     */
    public DatabaseArtistLoadTask(Context context, String requestType) {
        super();
        this.context = context;
        this.requestType = requestType;
    }

    @Override
    protected Boolean loadArtists() {
        final ArtistReaderContract.ArtistReaderDbHelper helper = new ArtistReaderContract.ArtistReaderDbHelper(context);
        Cursor cursor = helper.getCursorWithType(requestType);

        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                publishProgress(helper.getArtist(cursor));
                cursor.moveToNext();
            }
            return true;
        }
        return false;
    }
}
