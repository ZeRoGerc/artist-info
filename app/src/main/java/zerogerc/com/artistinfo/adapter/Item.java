package zerogerc.com.artistinfo.adapter;

/**
 * Items, BaseAdapter could handle
 *
 * Actions to implement new type of Item:
 *  1. Add new constant
 *  2. Inherit new class from Item
 *  3. getType() must return constant added above
 *  4. Implement BaseViewHolder for this new type
 */

public interface Item {
    int ARTIST = 1;

    int getType();
}

