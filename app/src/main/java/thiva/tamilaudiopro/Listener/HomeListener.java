package thiva.tamilaudiopro.Listener;

import thiva.tamilaudiopro.item.ItemAlbums;
import thiva.tamilaudiopro.item.ItemArtist;
import thiva.tamilaudiopro.item.ItemHomeBanner;
import thiva.tamilaudiopro.item.ItemSong;

import java.util.ArrayList;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public interface HomeListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemHomeBanner> arrayListBanner, ArrayList<ItemAlbums> arrayListAlbums, ArrayList<ItemArtist> arrayListArtist, ArrayList<ItemSong> arrayListSongs, ArrayList<ItemSong> arrayListlatest);
}
