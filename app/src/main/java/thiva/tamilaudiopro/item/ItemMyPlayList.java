package thiva.tamilaudiopro.item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Company : Nemosofts
 * Detailed : Software Development Company in Sri Lanka
 * Developer : Thivakaran
 * Contact : thivakaran829@gmail.com
 * Contact : nemosofts@gmail.com
 * Website : https://nemosofts.com
 */

public class ItemMyPlayList implements Serializable{

	private String id, name;
	private ArrayList<String> arrayListUrl;

	public ItemMyPlayList(String id, String name, ArrayList<String> arrayListUrl) {
		this.id = id;
		this.name = name;
		this.arrayListUrl = arrayListUrl;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getArrayListUrl() {
		return arrayListUrl;
	}
}
