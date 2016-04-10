package zerogerc.com.artistinfo;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZeRoGerc on 11/04/16.
 */
public class ArtistsLoadTask extends AsyncTask<Void, Artist, List<Artist>> {
    private List<Artist> artistsList;
    private RecyclerView.Adapter adapter;

    public ArtistsLoadTask(List<Artist> artists, RecyclerView recyclerView) {
        this.artistsList = artists;
        this.adapter = recyclerView.getAdapter();
    }

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
                case "name" :
                    artist.setName(reader.nextString());
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

    private void readCovers(final JsonReader reader, final Artist artist) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.nextName().equals("small")) {
                artist.setSmallPicAddress(reader.nextString());
            } else {
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
        artistsList.add(values[0]);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(List<Artist> artists) {
        super.onPostExecute(artists);
    }
}
