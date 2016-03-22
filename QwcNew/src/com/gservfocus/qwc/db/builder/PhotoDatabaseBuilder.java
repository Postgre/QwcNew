/*
 * @(#)AccountDatabaseBuilder.java 2011-2-25
 *
 * Copyright 2006-2011 YiMing Wang, All Rights reserved.
 */
package com.gservfocus.qwc.db.builder;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Base64;

import com.gservfocus.qwc.bean.Account;
import com.gservfocus.qwc.bean.Photo;
import com.gservfocus.qwc.bean.Scenic;

/**
 *
 * @author wang
 */
public class PhotoDatabaseBuilder extends DatabaseBuilder<Photo> {

	@Override
	public Photo build(Cursor c) {
		// TODO Auto-generated method stub
		int columnResource = c.getColumnIndex("resource");
		
		String resource = c.getString(columnResource);
		byte[] data = Base64.decode(resource, Base64.DEFAULT);
		
		Photo photo = Photo.serializable4Data(data);
		return photo;
	}

	@Override
	public ContentValues deconstruct(Photo t,String id) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		byte[] data = Photo.serializable2Data(t);
		values.put("id", id);
		values.put("resource", Base64.encodeToString(data, Base64.DEFAULT));
		return values;
	}

}
