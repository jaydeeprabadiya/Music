package thiva.tamilaudiopro.item;

import java.util.ArrayList;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ItemHomeBanner {

	private String id, title, image, message, totalSong;
	private ArrayList<ItemSong> arrayListSongs;

	public ItemHomeBanner(String id, String title, String image, String message, String totalSong, ArrayList<ItemSong> arrayListSongs) {
		this.id = id;
		this.title = title;
		this.image = image;
		this.message = message;
		this.totalSong = totalSong;
		this.arrayListSongs = arrayListSongs;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getImage() {
		return image;
	}

	public String getDesc() {
		return message;
	}

	public String getTotalSong() {
		return totalSong;
	}

	public ArrayList<ItemSong> getArrayListSongs() {
		return arrayListSongs;
	}
}