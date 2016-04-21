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
 * Created by ZeRoGerc on 22/04/16.
 */
public final class ArtistReaderContract {
    public ArtistReaderContract() {}

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
     * Available types of column {@link zerogerc.com.artistinfo.database.ArtistReaderContract.ArtistEntry#COLUMN_NAME_REQUEST_TYPE}.
     */
    public static final String REQUEST_TYPE_FAVOURITES = "favourites";
    public static final String REQUEST_TYPE_RECENT = "recent";


    public static class ArtistReaderDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "ArtistReader.db";

        public ArtistReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        private ContentValues getContentValues(Artist artist) {
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

        public long insertArtistFavourites(Artist artist) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = getContentValues(artist);
            contentValues.put(ArtistEntry.COLUMN_NAME_REQUEST_TYPE, REQUEST_TYPE_FAVOURITES);
            return db.insert(ArtistEntry.TABLE_NAME, null, contentValues);
        }

        public long insertArtistRecent(Artist artist) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = getContentValues(artist);
            contentValues.put(ArtistEntry.COLUMN_NAME_REQUEST_TYPE, REQUEST_TYPE_RECENT);
            return db.insert(ArtistEntry.TABLE_NAME, null, contentValues);
        }

        private Cursor getCursorWithType(final String requestType) {
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
         * @param requestType type of request(can be either {@link #REQUEST_TYPE_FAVOURITES} or {@link #REQUEST_TYPE_RECENT})
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

        private String packList(List<String> list) {
            return TextUtils.join(",", list);
        }

        private List<String> unpackList(String pack) {
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

        private Artist getArtist(final Cursor cursor) {
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
    }
}
