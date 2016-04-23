package zerogerc.com.artistinfo.extra;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import zerogerc.com.artistinfo.Artist;
import zerogerc.com.artistinfo.R;

/**
 * Created by ZeRoGerc on 11/04/16.
 */
public class Utils {
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics));
    }

    public static int pixelsToDip(Context context, float pixel)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return ((int)(pixel * scale + 0.5f));
    }

    public static String getTrackAlbumsString(Context context, Artist artist) {
        final String tracks = Integer.toString(artist.getTracks()) + " " + context.getString(R.string.tracks);
        final String albums = Integer.toString(artist.getAlbums()) + " " + context.getString(R.string.albums);

        return tracks + " \u2022 " + albums;
    }
}
