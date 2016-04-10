package zerogerc.com.artistinfo;

import zerogerc.com.artistinfo.adapter.Item;

/**
 * Created by ZeRoGerc on 10/04/16.
 */
public class Artist implements Item
{
    private String name;
    private String smallPicAddress;

    public Artist() {}

    public Artist(String name, String smallPicAddress) {
        this.name = name;
        this.smallPicAddress = smallPicAddress;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getSmallPicAddress() {
        return smallPicAddress;
    }

    public void setSmallPicAddress(String smallPicAddress) {
        this.smallPicAddress = smallPicAddress;
    }

    @Override
    public int getType() {
        return Item.ARTIST;
    }
}
