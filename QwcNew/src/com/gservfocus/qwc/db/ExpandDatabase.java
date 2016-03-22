package com.gservfocus.qwc.db;

import java.util.ArrayList;

import com.gservfocus.qwc.bean.Account;
import com.gservfocus.qwc.bean.Photo;
import com.gservfocus.qwc.bean.Scenic;

public interface ExpandDatabase {
	
	/**
	 * Close database
	 */
	void finalize();
	
	/**
	 * Get all photo to the database
	 * @return
	 */
	ArrayList<Photo> allPhotos() ;

	/**
	 * Save photo to the database
	 * 
	 * @param photo
	 * @return
	 */
	boolean savePhoto(Photo photo);
	/**
	 * remove photo to the database
	 * 
	 * @param photo
	 * @return
	 */
	boolean removePhoto(ArrayList<Photo> photos);
	
}
