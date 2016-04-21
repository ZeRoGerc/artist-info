package zerogerc.com.artistinfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import zerogerc.com.artistinfo.adapter.Item;

/**
 * Created by ZeRoGerc on 10/04/16.
 */
public class Artist implements Item, Parcelable
{
    private int id;
    private String name;
    private List<String> genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private String smallPicAddress;
    private String bigPicAddress;

    public Artist() {}

    public Artist(Parcel source) {
        this.genres = new ArrayList<>();
        this.id = source.readInt();
        this.name = source.readString();
        source.readStringList(this.genres);
        this.tracks = source.readInt();
        this.albums = source.readInt();
        this.link = source.readString();
        this.description = source.readString();
        this.smallPicAddress = source.readString();
        this.bigPicAddress = source.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public int getAlbums() {
        return albums;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSmallPicAddress() {
        return smallPicAddress;
    }

    public void setSmallPicAddress(String smallPicAddress) {
        this.smallPicAddress = smallPicAddress;
    }

    public String getBigPicAddress() {
        return bigPicAddress;
    }

    public void setBigPicAddress(String bigPicAddress) {
        this.bigPicAddress = bigPicAddress;
    }

    @Override
    public int getType() {
        return Item.ARTIST;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeStringList(genres);
        dest.writeInt(tracks);
        dest.writeInt(albums);
        dest.writeString(link);
        dest.writeString(description);
        dest.writeString(smallPicAddress);
        dest.writeString(bigPicAddress);
    }

    public static final Parcelable.Creator<Artist> CREATOR
            = new Parcelable.Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
