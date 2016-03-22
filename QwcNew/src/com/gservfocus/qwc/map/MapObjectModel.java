package com.gservfocus.qwc.map;

import android.location.Location;

public class MapObjectModel 
{
	private int x;
	private int y;
	private int id;
	private boolean isScan;
	private String caption;
	private Location location;
	
	public MapObjectModel(int id, Location location, String caption)
	{
		this.location = location;
		this.caption = caption;
		this.id = id;
	}
	
	public MapObjectModel(int id, int x, int y, String caption,boolean isScan)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.caption = caption;
		this.isScan = isScan;
	}

	public int getId() 
	{
		return id;
	}

	
	public int getX() 
	{
		return x;
	}


	public int getY() 
	{
		return y;
	}
	
	
	public Location getLocation()
	{
		return location;
	}
	
	
	public String getCaption()
	{
		return caption;
	}

	public boolean isScan() {
		return isScan;
	}

	public void setScan(boolean isScan) {
		this.isScan = isScan;
	}

}
