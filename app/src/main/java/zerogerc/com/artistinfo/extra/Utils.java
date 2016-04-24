package zerogerc.com.artistinfo.extra;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.R;

/**
 * Class with bunch of useful methods.
 */
public class Utils {
    /**
     * Converts dp to px
     * @param context current context
     * @param dp size in dp
     * @return size in px
     */
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Provides formatted string with tracks and albums of given artist.
     * This text is for showing in {@link android.widget.TextView} on UI.
     * @param context current context
     * @param artist given artist
     * @return formatted text
     */
    public static String getTrackAlbumsString(Context context, final Artist artist) {
        final String tracks = Integer.toString(artist.getTracks()) + " " + context.getString(R.string.tracks);
        final String albums = Integer.toString(artist.getAlbums()) + " " + context.getString(R.string.albums);

        return tracks + " \u2022 " + albums;
    }

    /**
     * Converts first letter of given text to uppercase.
     * @param text given text
     * @return formatted text
     */
    public static String firstToUpperCase(final String text) {
        return Character.toString(text.charAt(0)).toUpperCase() + text.substring(1);
    }

    /**
     * Provide sting with representation of all genres of given artist.
     * This text is for showing in {@link android.widget.TextView} on UI.
     * @param artist given artist
     * @return formatted genres
     */
    public static String getGenresString(final Artist artist) {
        return TextUtils.join(", ", artist.getGenres().toArray());
    }
}
