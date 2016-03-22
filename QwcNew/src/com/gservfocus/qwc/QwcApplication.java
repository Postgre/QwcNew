package com.gservfocus.qwc;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.gservfocus.qwc.bean.Account;
import com.gservfocus.qwc.bean.Teacher;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class QwcApplication extends Application {
	// APP_ID 替换为你的应用从官方网站申请到的合法appId
	public static final String APP_ID = "wx397097ba83604ec9";
	// IWXAPI 是第三方app和微信通信的openapi接口
	// private IWXAPI api;
	/** 单例模式 */
	private static QwcApplication instance;

	public static QwcApplication getInstance() {

		return instance;

	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initImageLoader();
	}

	private void initImageLoader() {

		/*
		 * DisplayImageOptions defaultOptions = new
		 * DisplayImageOptions.Builder()
		 * .showImageForEmptyUri(R.drawable.empty_photo)
		 * .showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
		 * .cacheOnDisc(true).build(); ImageLoaderConfiguration config = new
		 * ImageLoaderConfiguration.Builder( getApplicationContext())
		 * .defaultDisplayImageOptions(defaultOptions) .discCacheSize(50 * 1024
		 * * 1024).discCacheFileCount(100) .writeDebugLogs().build();
		 * ImageLoader.getInstance().init(config);
		 */
		File cacheDir = StorageUtils.getCacheDirectory(getBaseContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getBaseContext())
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.diskCacheExtraOptions(480, 800, null)
				.threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
				// default
				.diskCache(new UnlimitedDiscCache(cacheDir))
				// default
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(getBaseContext())) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	public void saveAccount(Account account) {
		SharedPreferences preferences = getSharedPreferences("user_info",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("account", account.getAccount());
		editor.putString("imageUrl", account.getImageUrl());
		editor.putString("password", account.getPassword());
		editor.putString("mobile", account.getMobile());
		editor.putString("integral", account.getIntegral());
		editor.putString("rankName", account.getRankName());
		editor.putString("uid", account.getUserId());
		editor.commit();
	}

	public Account getAccount() {
		SharedPreferences preferences = getSharedPreferences("user_info",
				Context.MODE_PRIVATE);
		Account accout = null;
		if (!TextUtils.isEmpty(preferences.getString("account", ""))) {
			accout = new Account();
			accout.setAccount(preferences.getString("account", ""));
			accout.setImageUrl(preferences.getString("imageUrl", ""));
			accout.setPassword(preferences.getString("password", ""));
			accout.setMobile(preferences.getString("mobile", ""));
			accout.setIntegral(preferences.getString("integral", ""));
			accout.setRankName(preferences.getString("rankName", ""));
			accout.setUserId(preferences.getString("uid", ""));
		}

		return accout;
	}

	public void clearAccount() {
		SharedPreferences preferences = getSharedPreferences("user_info",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	

}
