package com.gservfocus.qwc.bean;


public class GridViewModel {
	private int  id;				//唯一ID
	private String itemText;		//标题文字
	private Integer itemImageId;	//显示的图片
	
	public GridViewModel(int id,String text,Integer imageId){
		this.setId(id);
		this.setItemImageId(imageId);
		this.setItemText(text);
	}

	public String getItemText() {
		return itemText;
	}
	public void setItemText(String itemText) {
		this.itemText = itemText;
	}
	public Integer getItemImageId() {
		return itemImageId;
	}
	public void setItemImageId(Integer itemImageId) {
		this.itemImageId = itemImageId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
