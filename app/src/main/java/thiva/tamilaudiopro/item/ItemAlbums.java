package thiva.tamilaudiopro.item;

import java.io.Serializable;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ItemAlbums implements Serializable {

	private String id,name, image, thumb, artist = "";

	public ItemAlbums(String id, String name, String image, String thumb) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.thumb = thumb;
	}

	public ItemAlbums(String id, String name, String image, String thumb, String artist) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.thumb = thumb;
		this.artist = artist;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return image;
	}

	public String getThumb() {
		return thumb;
	}

	public String getArtist() {
		return artist;
	}
}