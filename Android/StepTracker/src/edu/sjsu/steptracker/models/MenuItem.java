package edu.sjsu.steptracker.models;

public class MenuItem {
	private int imgId;
	private String itemName;

	public MenuItem(int imgId, String itemName) {
		this.setImgId(imgId);
		this.setItemName(itemName);
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
