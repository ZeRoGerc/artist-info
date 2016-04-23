package zerogerc.com.artistinfo.loaders;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import zerogerc.com.artistinfo.Artist;

/**
 * Provide loading of artists from network.
 * Load data from <code>http://cache-spb04.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json</code>
 */
public class NetworkArtistLoadTask extends ArtistsLoadTask {
    protected Boolean loadArtists() {
        InputStream inputStream = null;
        try {
            inputStream = new URL("http://cache-spb04.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json").openStream();
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            readArtistsList(reader);

            return true;

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

        return false;
    }

    private void readArtistsList(final JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            publishProgress(readArtist(reader));
        }
        reader.endArray();
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
}
