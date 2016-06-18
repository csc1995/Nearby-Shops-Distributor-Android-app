package org.localareadelivery.distributorapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.List;



public class Shop implements Parcelable{


	// real time variables
	double distance;

	int shopID;
	
	String shopName;

	// New Paramaters

	// the radius of the circle considering shop location as its center.
	//This is the distance upto which shop can deliver its items
	double deliveryRange;

	// latitude and longitude for storing the location of the shop
	double latCenter;
	double lonCenter;

	double latMax;
	double lonMax;
	double latMin;
	double lonMin;

	// delivery charger per order 
	double deliveryCharges;

	int distributorID;

	String imagePath;




	// added recently
	String shopAddress;
	String city;
	long pincode;
	String landmark;
	int billAmountForFreeDelivery;
	String customerHelplineNumber;
	String deliveryHelplineNumber;
	String shortDescription;
	String longDescription;
	Timestamp dateTimeStarted;
	boolean isOpen;





	// Getter and Setter Methods


	protected Shop(Parcel in) {
		distance = in.readDouble();
		shopID = in.readInt();
		shopName = in.readString();
		deliveryRange = in.readDouble();
		latCenter = in.readDouble();
		lonCenter = in.readDouble();
		latMax = in.readDouble();
		lonMax = in.readDouble();
		latMin = in.readDouble();
		lonMin = in.readDouble();
		deliveryCharges = in.readDouble();
		distributorID = in.readInt();
		imagePath = in.readString();
		shopAddress = in.readString();
		city = in.readString();
		pincode = in.readLong();
		landmark = in.readString();
		billAmountForFreeDelivery = in.readInt();
		customerHelplineNumber = in.readString();
		deliveryHelplineNumber = in.readString();
		shortDescription = in.readString();
		longDescription = in.readString();
		isOpen = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(distance);
		dest.writeInt(shopID);
		dest.writeString(shopName);
		dest.writeDouble(deliveryRange);
		dest.writeDouble(latCenter);
		dest.writeDouble(lonCenter);
		dest.writeDouble(latMax);
		dest.writeDouble(lonMax);
		dest.writeDouble(latMin);
		dest.writeDouble(lonMin);
		dest.writeDouble(deliveryCharges);
		dest.writeInt(distributorID);
		dest.writeString(imagePath);
		dest.writeString(shopAddress);
		dest.writeString(city);
		dest.writeLong(pincode);
		dest.writeString(landmark);
		dest.writeInt(billAmountForFreeDelivery);
		dest.writeString(customerHelplineNumber);
		dest.writeString(deliveryHelplineNumber);
		dest.writeString(shortDescription);
		dest.writeString(longDescription);
		dest.writeByte((byte) (isOpen ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Shop> CREATOR = new Creator<Shop>() {
		@Override
		public Shop createFromParcel(Parcel in) {
			return new Shop(in);
		}

		@Override
		public Shop[] newArray(int size) {
			return new Shop[size];
		}
	};




	/*
	* Getter and Setter methods
	*
	* */

	public Shop() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getDeliveryRange() {
		return deliveryRange;
	}

	public void setDeliveryRange(double deliveryRange) {
		this.deliveryRange = deliveryRange;
	}

	public double getLatCenter() {
		return latCenter;
	}

	public void setLatCenter(double latCenter) {
		this.latCenter = latCenter;
	}

	public double getLonCenter() {
		return lonCenter;
	}

	public void setLonCenter(double lonCenter) {
		this.lonCenter = lonCenter;
	}

	public double getLatMax() {
		return latMax;
	}

	public void setLatMax(double latMax) {
		this.latMax = latMax;
	}

	public double getLonMax() {
		return lonMax;
	}

	public void setLonMax(double lonMax) {
		this.lonMax = lonMax;
	}

	public double getLatMin() {
		return latMin;
	}

	public void setLatMin(double latMin) {
		this.latMin = latMin;
	}

	public double getLonMin() {
		return lonMin;
	}

	public void setLonMin(double lonMin) {
		this.lonMin = lonMin;
	}

	public double getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(double deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public int getDistributorID() {
		return distributorID;
	}

	public void setDistributorID(int distributorID) {
		this.distributorID = distributorID;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public long getPincode() {
		return pincode;
	}

	public void setPincode(long pincode) {
		this.pincode = pincode;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public int getBillAmountForFreeDelivery() {
		return billAmountForFreeDelivery;
	}

	public void setBillAmountForFreeDelivery(int billAmountForFreeDelivery) {
		this.billAmountForFreeDelivery = billAmountForFreeDelivery;
	}

	public String getCustomerHelplineNumber() {
		return customerHelplineNumber;
	}

	public void setCustomerHelplineNumber(String customerHelplineNumber) {
		this.customerHelplineNumber = customerHelplineNumber;
	}

	public String getDeliveryHelplineNumber() {
		return deliveryHelplineNumber;
	}

	public void setDeliveryHelplineNumber(String deliveryHelplineNumber) {
		this.deliveryHelplineNumber = deliveryHelplineNumber;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public Timestamp getDateTimeStarted() {
		return dateTimeStarted;
	}

	public void setDateTimeStarted(Timestamp dateTimeStarted) {
		this.dateTimeStarted = dateTimeStarted;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean open) {
		isOpen = open;
	}


	// Autogenerated parcelable implementation




	@Override
	public String toString() {
		super.toString();


		String resultMessage = "ID: " + this.getShopID()
				+ "\n\nShop Name : " + this.getShopName()
				+ "\n\nDelivery Range : " + this.getDeliveryRange()
				+ "\n\nLat Center: " + this.getLatCenter()
				+ "\n\nLon Center : " + this.getLonCenter()
				+ "\n\nLon Max : " + this.getLonMax()
				+ "\n\nLat Max : " + this.getLatMin()
				+ "\n\nLon Min : " + this.getLonMax()
				+ "\n\nLat Min : " + this.getLatMin()
				+ "\n\nDelivery Charges : " + this.getDeliveryCharges()
				+ "\n\nDistributor ID : " + this.getDistributorID()
				+ "\n\nImage Path : " + this.getImagePath();


		return resultMessage;
	}


}
