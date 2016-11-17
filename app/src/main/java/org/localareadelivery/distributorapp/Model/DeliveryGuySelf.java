package org.localareadelivery.distributorapp.Model;


import android.os.Parcel;
import android.os.Parcelable;

import org.localareadelivery.distributorapp.Model.Shop;

/**
 * Created by sumeet on 14/6/16.
 */
public class DeliveryGuySelf implements Parcelable{

    // Table Name
    public static final String TABLE_NAME = "DELIVERY_GUY_SELF";

    // column Names
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String SHOP_ID = "SHOP_ID";

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";

    public static final String ABOUT = "ABOUT";
    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";

    // to be Implemented
    public static final String IS_ENABLED = "IS_ENABLED";
    public static final String IS_WAITLISTED = "IS_WAITLISTED";

    public DeliveryGuySelf() {
    }


    // instance Variables

    private int deliveryGuyID;
    private String name;
    private int shopID;

    private String username;
    private String password;

    private String about;
    private String profileImageURL;
    private String phoneNumber;

    private boolean isEnabled;
    private boolean isWaitlisted;


    protected DeliveryGuySelf(Parcel in) {
        deliveryGuyID = in.readInt();
        name = in.readString();
        shopID = in.readInt();
        username = in.readString();
        password = in.readString();
        about = in.readString();
        profileImageURL = in.readString();
        phoneNumber = in.readString();
        isEnabled = in.readByte() != 0;
        isWaitlisted = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(deliveryGuyID);
        dest.writeString(name);
        dest.writeInt(shopID);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(about);
        dest.writeString(profileImageURL);
        dest.writeString(phoneNumber);
        dest.writeByte((byte) (isEnabled ? 1 : 0));
        dest.writeByte((byte) (isWaitlisted ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryGuySelf> CREATOR = new Creator<DeliveryGuySelf>() {
        @Override
        public DeliveryGuySelf createFromParcel(Parcel in) {
            return new DeliveryGuySelf(in);
        }

        @Override
        public DeliveryGuySelf[] newArray(int size) {
            return new DeliveryGuySelf[size];
        }
    };

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDeliveryGuyID() {
        return deliveryGuyID;
    }

    public void setDeliveryGuyID(int deliveryGuyID) {
        this.deliveryGuyID = deliveryGuyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getWaitlisted() {
        return isWaitlisted;
    }

    public void setWaitlisted(Boolean waitlisted) {
        isWaitlisted = waitlisted;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }
}
