package org.localareadelivery.distributorapp.Model;


import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable{

	int itemID;
	
	String itemName;
	String itemDescription;
	String itemImageURL;
	
	//technically it is the name of the manufacturer
	String brandName;


	int itemCategoryID;
	
	
	//List<OrderItem> orderItems;
	
	
	//List<ShopItem> shopItems;
	
	// the category in which this item is contained
	//(cascade = CascadeType.ALL)
	


	ItemCategory itemCategory;





	// parcelable interface implementation

	/*

	 Parcelable Interface Implementation

	 */



	protected Item(Parcel in) {
		itemID = in.readInt();
		itemName = in.readString();
		itemDescription = in.readString();
		itemImageURL = in.readString();
		brandName = in.readString();
		itemCategoryID = in.readInt();
		itemCategory = in.readParcelable(ItemCategory.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(itemID);
		dest.writeString(itemName);
		dest.writeString(itemDescription);
		dest.writeString(itemImageURL);
		dest.writeString(brandName);
		dest.writeInt(itemCategoryID);
		dest.writeParcelable(itemCategory, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Item> CREATOR = new Creator<Item>() {

		@Override
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}

		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}

	};









	/*

	// Getter and Setter Methods

	 */



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

	public int getItemCategoryID() {
		return itemCategoryID;
	}

	public void setItemCategoryID(int itemCategoryID) {
		this.itemCategoryID = itemCategoryID;
	}

}
