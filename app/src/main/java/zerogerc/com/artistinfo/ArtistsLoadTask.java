package zerogerc.com.artistinfo;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import zerogerc.com.artistinfo.adapter.BaseAdapter;

/**
 * Created by ZeRoGerc on 11/04/16.
 */
public class ArtistsLoadTask extends AsyncTask<Void, Artist, List<Artist>> {
    public static final  String LOG_TAG = "ArtistLoadTask";

    private BaseAdapter<? super Artist> adapter;

    public ArtistsLoadTask() {}

    public List<Artist> loadArtists() {
        InputStream inputStream = null;
        try {
            inputStream = new URL("http://cache-spb04.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json").openStream();
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            return readArtistsList(reader);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    //TODO: shit happens
                }
            }
        }

        return new ArrayList<>();
    }

    private List<Artist> readArtistsList(final JsonReader reader) throws IOException {
        final List<Artist> artists = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            artists.add(readArtist(reader));
            publishProgress(artists.get(artists.size() - 1));
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        reader.endArray();
        return artists;
    }

    private Artist readArtist(final JsonReader reader) throws IOException {
        final Artist artist = new Artist();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id" :
                    artist.setId(reader.nextInt());
                    break;
                case "name" :
                    artist.setName(reader.nextString());
                    break;
                case "genres" :
                    artist.setGenres(readGenres(reader));
                    break;
                case "tracks" :
                    artist.setTracks(reader.nextInt());
                    break;
                case "albums" :
                    artist.setAlbums(reader.nextInt());
                    break;
                case "link" :
                    artist.setLink(reader.nextString());
                    break;
                case "description" :
                    artist.setDescription(reader.nextString());
                    break;
                case "cover" :
                    readCovers(reader, artist);
                    break;
                default :
                    reader.skipValue();
            }
        }
        reader.endObject();
        return artist;
    }

    private List<String> readGenres(final JsonReader reader) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            result.add(reader.nextString());
        }
        reader.endArray();
        return result;
    }

    private void readCovers(final JsonReader reader, final Artist artist) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "small" :
                    artist.setSmallPicAddress(reader.nextString());
                    break;
                case "big" :
                    artist.setBigPicAddress(reader.nextString());
                    break;
                default :
                    reader.skipValue();
            }
        }
        reader.endObject();
    }

    @Override
    protected List<Artist> doInBackground(Void... params) {
        return loadArtists();
    }

    @Override
    protected void onProgressUpdate(Artist... values) {
        super.onProgressUpdate(values);
        if (adapter != null) {
            adapter.append(values[0]);
        } else {
            Log.e(LOG_TAG, "Null adapter");
        }
    }

    @Override
    protected void onPostExecute(List<Artist> artists) {
        super.onPostExecute(artists);
    }

    public void setAdapter(BaseAdapter<? super Artist> adapter) {
        this.adapter = adapter;
    }
}
