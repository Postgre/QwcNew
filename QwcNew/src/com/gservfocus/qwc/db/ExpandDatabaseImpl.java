package com.gservfocus.qwc.db;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import com.gservfocus.qwc.bean.Photo;
import com.gservfocus.qwc.bean.Scenic;
import com.gservfocus.qwc.db.builder.PhotoDatabaseBuilder;

public class ExpandDatabaseImpl implements ExpandDatabase {
	private static final String DB_NAME = "/cache.db";
	// 数据库路径
	public static final String DB_PATH = Environment
				.getExternalStorageDirectory().getAbsolutePath() + "/qwstc";
	private static final String TABLE_PHOTO_LIBRARY = "photo_table";
	private static final int DB_VERSION = 2;

	private String mPath;
	private SQLiteDatabase mDb;

	/**
	 * Default constructor
	 * 
	 * @param path
	 *            Database file
	 */
	public ExpandDatabaseImpl(String path) {
		mPath = path;
		mDb = getDb();
		if (mDb == null) {
			return;
		}
		if (mDb.getVersion() < DB_VERSION) {
			System.out.println("当前数据库版本:" + mDb.getVersion());
			new UpdaterBuilder().getUpdater(DB_VERSION).update();
		}
	}

	private synchronized SQLiteDatabase getDb() {
		boolean success = (new File(mPath)).mkdirs();
		if (success) {
			Log.i(this.getClass().getSimpleName(), "Directory: " + mPath + " created");
		}
		try {
			return SQLiteDatabase.openDatabase(mPath + DB_NAME, null,
					SQLiteDatabase.CREATE_IF_NECESSARY);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void finalize() {
		if (mDb != null) {
			mDb.close();
		}
	}
	
	public synchronized ArrayList<Photo> allPhotos() {
		// TODO Auto-generated method stub
		ArrayList<Photo> photos = new ArrayList<Photo>();
		if (mDb == null) {
			return photos;
		}

		Cursor query = mDb.query(TABLE_PHOTO_LIBRARY, null, null, null, null, null,
				null);
		if (query.moveToFirst()) {
			while (!query.isAfterLast()) {
				photos.add(new PhotoDatabaseBuilder().build(query));
				query.moveToNext();
			}
		}
		query.close();
		finalize();
		return photos;
	}

	public synchronized boolean savePhoto(Photo photo) {
		// TODO Auto-generated method stub
		if (mDb == null) {
			// database was not created
			return false;
		}
		ContentValues values = new ContentValues();
		values.putAll(new PhotoDatabaseBuilder().deconstruct(photo,photo.getPath()));
		String[] whereArgs = { photo.getPath()};
		 
		 int row =  mDb.update(TABLE_PHOTO_LIBRARY, values, "id=?", whereArgs);
		 if(row == 0){
			 mDb.insert(TABLE_PHOTO_LIBRARY, null, values);
		 }

		finalize();
		return true;
	}
	/**
	 * remove photo to the database
	 * 
	 * @param photo
	 * @return
	 */
	public synchronized boolean removePhoto(ArrayList<Photo> photos){
		if (mDb == null) {
			// database was not created
			return false;
		}
		
		for (Photo photo : photos) {

			String[] whereArgs = { photo.getPath()};
			mDb.delete(TABLE_PHOTO_LIBRARY, "id=?", whereArgs);
		}
		finalize();
		return true;
	}
	/**
	 * Updater from version 0 to 1.
	 * 
	 * Updates from previous version without copying data. Creates database if
	 * it doesn't exist.
	 * 
	 * @author yiming wang
	 * 
	 */
	private class DatabaseUpdaterV1 extends DatabaseUpdater {

		// TODO Add copying and updating data from previous db version

		private int version = 1;

		public DatabaseUpdaterV1(int version) {
			this.version = version;
		}

		@SuppressWarnings("unused")
		public DatabaseUpdaterV1(DatabaseUpdater updater) {
			setUpdater(updater);
		}

		public void update() {
			if (getUpdater() != null) {
				getUpdater().update();
			}
			try {
				mDb.execSQL("DROP TABLE " + TABLE_PHOTO_LIBRARY + ";");

			} catch (SQLiteException e) {
				Log.v(this.getClass().getSimpleName(), "Library table not existing");
			}
			createTables();
			mDb.setVersion(version);
		}

		private void createTables() {
			System.out.println("数据库更新====table");
			mDb.execSQL("CREATE TABLE IF NOT EXISTS "
					+ TABLE_PHOTO_LIBRARY
					+ " (id VARCHAR, resource VARCHAR);");
		}
	}

	/**
	 * DatabaseUpdater Builder. Builds an updater for updating database from its
	 * current version to version passed as parameter of getUpadater method.
	 * 
	 * Each updater uses a previous db version as input. Builder builds a
	 * decorated updater to update to the desired version.
	 * 
	 * @author yiming wang
	 * 
	 */
	private class UpdaterBuilder {

		public DatabaseUpdater getUpdater(int version) {
			DatabaseUpdater updater = new DatabaseUpdaterV1(version);
			/*
			 * switch (version) { case 1: updater = new DatabaseUpdaterV1();
			 * break; case 0: updater = null; break; } if (version >
			 * mDb.getVersion()+1) { updater.setUpdater(getUpdater(version -
			 * 1)); }
			 */
			return updater;
		}
	}

	

}

abstract class DatabaseUpdater {
	private DatabaseUpdater mUpdater;

	abstract void update();

	public void setUpdater(DatabaseUpdater mUpdater) {
		this.mUpdater = mUpdater;
	}

	public DatabaseUpdater getUpdater() {
		return mUpdater;
	}

}
