package org.localareadelivery.distributorapp.Model;


public class Item {

	int itemID;
	
	String itemName;
	String itemDescription;
	String itemImageURL;
	
	//technically it is the name of the manufacturer
	String brandName;
	
	
	//List<OrderItem> orderItems;
	
	
	//List<ShopItem> shopItems;
	
	// the category in which this item is contained
	//(cascade = CascadeType.ALL)
	


	ItemCategory itemCategory;



	public ItemCategory getItemCategory() {
		return itemCategory;
	}



	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}


	
	//No-args constructor

	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	// getters and Setter methods

	public int getItemID() {
		return itemID;
	}



	public void setItemID(int itemID) {
		this.itemID = itemID;
	}



	public String getItemDescription() {
		return itemDescription;
	}



	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}



	public String getItemImageURL() {
		return itemImageURL;
	}



	public void setItemImageURL(String itemImageURL) {
		this.itemImageURL = itemImageURL;
	}



	public String getBrandName() {
		return brandName;
	}



	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
