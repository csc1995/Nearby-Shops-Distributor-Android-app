package org.localareadelivery.distributorapp.Model;

public class ItemCategory {

	
	int itemCategoryID;
	
	String categoryName;
	String categoryDescription;
	
	//AbstractCategory parentCategory;
	
	// items contained in this category
	//@OneToMany(mappedBy="itemCategory")
	
	//List<Item> items = new ArrayList<Item>();



	
	//no-args Constructor
	public ItemCategory() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	//Getters and Setters
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public int getItemCategoryID() {
		return itemCategoryID;
	}

	public void setItemCategoryID(int itemCategoryID) {
		this.itemCategoryID = itemCategoryID;
	}
	
}
