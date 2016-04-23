package zerogerc.com.artistinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zerogerc.com.artistinfo.Artist;

/**
 * Class for easy work with database which stores recent and favourites artist lists.
 */
public final class ArtistReaderContract {
    public ArtistReaderContract() {}

    /**
     * One entry of database. All fields is for creating entry from {@link Artist}
     */
    public static abstract class ArtistEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";

        public static final String COLUMN_NAME_REQUEST_TYPE = "request";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_ARTIST = "artist";
        public static final String COLUMN_NAME_GENRES = "genres";
        public static final String COLUMN_NAME_TRACKS = "tracks";
        public static final String COLUMN_NAME_ALBUMS = "albums";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_SMALLPIC = "small_pic";
        public static final String COLUMN_NAME_BIGPIC = "big_pic";

    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ArtistEntry.TABLE_NAME + " (" +
                    ArtistEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    ArtistEntry.COLUMN_NAME_REQUEST_TYPE + TEXT_TYPE + COMMA_SEP +
                    ArtistEntry.COLUMN_NAME_ID + INT_TYPE + COMMA_SEP +
                    ArtistEntry.COLUMN_NAME_ARTIST + TEXT_TYPE + COMMA_SEP +
                    ArtistEntry.COLUMN_NAME_GENRES + TEXT_TYPE + COMMA_SEP +
                    ArtistEntry.COLUMN_NAME_TRACKS + INT_TYPE + COMMA_SEP +
                    ArtistEntry.COLUMN_NAME_ALBUMS + INT_TYPE + COMMA_SEP +
                    ArtistEntry.COLUMN_NAME_LINK + TEXT_TYPE + COMMA_SEP +
                    ArtistEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    ArtistEntry.COLUMN_NAME_SMALLPIC + TEXT_TYPE + COMMA_SEP +
                    ArtistEntry.COLUMN_NAME_BIGPIC + TEXT_TYPE +
                    ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ArtistEntry.TABLE_NAME;


    /**
     * Available type of column {@link zerogerc.com.artistinfo.database.ArtistReaderContract.ArtistEntry#COLUMN_NAME_REQUEST_TYPE}.
     * If you use this key you will work with list of favourite artists.
     */
    public static final String REQUEST_TYPE_FAVOURITES = "favourites";

    /**
     * Available type of column {@link zerogerc.com.artistinfo.database.ArtistReaderContract.ArtistEntry#COLUMN_NAME_REQUEST_TYPE}.
     * If you use this key you will work with list of recent artists.
     */
    public static final String REQUEST_TYPE_RECENT = "recent";


    public static class ArtistReaderDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "ArtistReader.db";

        public ArtistReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        /**
         * Upgrades given database from <code>oldVersion</code> to <code>newVersion</code>
         * @param db given database
         * @param oldVersion - old version
         * @param newVersion - new version
         */
        @Override
        public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        private ContentValues getContentValues(final Artist artist) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ArtistEntry.COLUMN_NAME_ID, artist.getId());
            contentValues.put(ArtistEntry.COLUMN_NAME_ARTIST, artist.getName());
            contentValues.put(ArtistEntry.COLUMN_NAME_GENRES, packList(artist.getGenres()));
            contentValues.put(ArtistEntry.COLUMN_NAME_TRACKS, artist.getTracks());
            contentValues.put(ArtistEntry.COLUMN_NAME_ALBUMS, artist.getAlbums());
            contentValues.put(ArtistEntry.COLUMN_NAME_LINK, artist.getLink());
            contentValues.put(ArtistEntry.COLUMN_NAME_DESCRIPTION, artist.getDescription());
            contentValues.put(ArtistEntry.COLUMN_NAME_SMALLPIC, artist.getSmallPicAddress());
            contentValues.put(ArtistEntry.COLUMN_NAME_BIGPIC, artist.getBigPicAddress());
            return contentValues;
        }

        /**
         * Insert given artist in database.
         * @param artist given artist
         * @param requestType type of request. Can be either {@link #REQUEST_TYPE_FAVOURITES} or {@link #REQUEST_TYPE_RECENT}
         * @return
         */
        public long insertArtist(final Artist artist, final String requestType) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = getContentValues(artist);
            contentValues.put(ArtistEntry.COLUMN_NAME_REQUEST_TYPE, requestType);
            return db.insert(ArtistEntry.TABLE_NAME, null, contentValues);
        }

        /**
         * Get cursor to proper either favourites or recent.
         * @param requestType type of list. Can be either {@link #REQUEST_TYPE_RECENT} or {@link #REQUEST_TYPE_FAVOURITES}
         * @return cursor to given list
         */
        public Cursor getCursorWithType(final String requestType) {
            SQLiteDatabase db = getReadableDatabase();

            String[] args = {requestType};

            return db.query(ArtistEntry.TABLE_NAME,
                    null,
                    ArtistEntry.COLUMN_NAME_REQUEST_TYPE + "=?",
                    args,
                    null,
                    null,
                    ArtistEntry.COLUMN_NAME_ARTIST + " ASC"
            );
        }

        /**
         * Retrieve list of artist from database.
         * @param requestType type of request. Can be either {@link #REQUEST_TYPE_FAVOURITES} or {@link #REQUEST_TYPE_RECENT})
         * @return list of retrieved artists
         */
        public List<Artist> getArtists(final String requestType) {
            Cursor cursor = getCursorWithType(requestType);
            List<Artist> artists = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    artists.add(getArtist(cursor));
                    cursor.moveToNext();
                }
            }
            return artists;
        }

        private String packList(final List<String> list) {
            return TextUtils.join(",", list);
        }

        private List<String> unpackList(final String pack) {
            return Arrays.asList(TextUtils.split(pack, ","));
        }

        private String getString(final Cursor cursor, final String key) {
            int id = cursor.getColumnIndexOrThrow(key);
            return cursor.getString(id);
        }

        private int getInt(final Cursor cursor, final String key) {
            int id = cursor.getColumnIndexOrThrow(key);
            return cursor.getInt(id);
        }

        /**
         * Get artist which given current cursor points to.
         * @param cursor given cursor
         * @return artist which given cursor points to.
         */
        public Artist getArtist(final Cursor cursor) {
            Artist artist = new Artist();
            artist.setId(getInt(cursor, ArtistEntry.COLUMN_NAME_ID));
            artist.setName(getString(cursor, ArtistEntry.COLUMN_NAME_ARTIST));
            artist.setGenres(unpackList(getString(cursor, ArtistEntry.COLUMN_NAME_GENRES)));
            artist.setTracks(getInt(cursor, ArtistEntry.COLUMN_NAME_TRACKS));
            artist.setAlbums(getInt(cursor, ArtistEntry.COLUMN_NAME_ALBUMS));
            artist.setLink(getString(cursor, ArtistEntry.COLUMN_NAME_LINK));
            artist.setDescription(getString(cursor, ArtistEntry.COLUMN_NAME_DESCRIPTION));
            artist.setSmallPicAddress(getString(cursor, ArtistEntry.COLUMN_NAME_SMALLPIC));
            artist.setBigPicAddress(getString(cursor, ArtistEntry.COLUMN_NAME_BIGPIC));
            return artist;
        }

        /**
         * Check if given artist is in database.
         * @param artist given artist
         * @param requestType type of database to find artist in. Can be either {@link #REQUEST_TYPE_FAVOURITES} or {@link #REQUEST_TYPE_RECENT}
         * @return true if artist is in database
         */
        public boolean hasArtist(final Artist artist, final String requestType) {
            SQLiteDatabase db = getReadableDatabase();

            String[] args = {requestType, artist.getName()};

            Cursor c =  db.query(ArtistEntry.TABLE_NAME,
                    null,
                    ArtistEntry.COLUMN_NAME_REQUEST_TYPE + "=?" + " AND " + ArtistEntry.COLUMN_NAME_ARTIST + "=?",
                    args,
                    null,
                    null,
                    ArtistEntry.COLUMN_NAME_ARTIST + " ASC"
            );

            return c != null && c.moveToFirst();
        }

        /**
         * Delete entry of given artist from one of lists.
         * @param artist given artist
         * @param requestType type of list to delete artist from. Can be either {@link #REQUEST_TYPE_FAVOURITES} or {@link #REQUEST_TYPE_RECENT}
         * @return true if artist was deleted
         */
        public boolean deleteArtist(final Artist artist, final String requestType) {
            SQLiteDatabase db = getWritableDatabase();
            final String selection = ArtistEntry.COLUMN_NAME_ARTIST + "=?" + " AND " +
                    ArtistEntry.COLUMN_NAME_REQUEST_TYPE + "=?";
            final String[] selectionArgs = {artist.getName(), requestType};

            return db.delete(ArtistEntry.TABLE_NAME, selection, selectionArgs) > 0;
        }
    }
}
