package org.localareadelivery.distributorapp.ModelRoles;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;

import java.sql.Timestamp;

/**
 * Created by sumeet on 14/6/16.
 */
public class DeliveryGuySelf implements Parcelable{

    // Table Name
//    public static final String TABLE_NAME = "DELIVERY_GUY_SELF";

    // column Names
//    public static final String DELIVERY_GUY_SELF_ID = "DELIVERY_GUY_SELF_ID";
//    public static final String NAME = "NAME";
//    public static final String SHOP_ID = "SHOP_ID";
//
////    public static final String USERNAME = "USERNAME";
//    public static final String PASSWORD = "PASSWORD";
//
//    public static final String ABOUT = "ABOUT";
//    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";
//    public static final String PHONE_NUMBER = "PHONE_NUMBER";
//    public static final String DESIGNATION = "DESIGNATION";
//
//    public static final String CURRENT_LATITUDE = "CURRENT_LATITUDE";
//    public static final String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";

    // to be Implemented
//    public static final String IS_ENABLED = "IS_ENABLED";
//    public static final String IS_WAITLISTED = "IS_WAITLISTED";
//
//    public static final String ACCOUNT_PRIVATE = "ACCOUNT_PRIVATE";
//
//    public static final String GOVERNMENT_ID_NUMBER = "GOVERNMENT_ID_NUMBER";
//    public static final String GOVERNMENT_ID_NAME = "GOVERNMENT_ID_NAME";
//    public static final String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";

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
    private String designation;

    private double currentLatitude;
    private double currentLongitude;

    private boolean isEnabled;
    private boolean isWaitlisted;

    private boolean accountPrivate;

    private String govtIDName;
    private String govtIDNumber;
    private Timestamp timestampCreated;

    protected DeliveryGuySelf(Parcel in) {
        deliveryGuyID = in.readInt();
        name = in.readString();
        shopID = in.readInt();
        username = in.readString();
        password = in.readString();
        about = in.readString();
        profileImageURL = in.readString();
        phoneNumber = in.readString();
        designation = in.readString();
        currentLatitude = in.readDouble();
        currentLongitude = in.readDouble();
        isEnabled = in.readByte() != 0;
        isWaitlisted = in.readByte() != 0;
        accountPrivate = in.readByte() != 0;
        govtIDName = in.readString();
        govtIDNumber = in.readString();
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
        dest.writeString(designation);
        dest.writeDouble(currentLatitude);
        dest.writeDouble(currentLongitude);
        dest.writeByte((byte) (isEnabled ? 1 : 0));
        dest.writeByte((byte) (isWaitlisted ? 1 : 0));
        dest.writeByte((byte) (accountPrivate ? 1 : 0));
        dest.writeString(govtIDName);
        dest.writeString(govtIDNumber);
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

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isWaitlisted() {
        return isWaitlisted;
    }

    public void setWaitlisted(boolean waitlisted) {
        isWaitlisted = waitlisted;
    }

    public boolean isAccountPrivate() {
        return accountPrivate;
    }

    public void setAccountPrivate(boolean accountPrivate) {
        this.accountPrivate = accountPrivate;
    }

    public String getGovtIDName() {
        return govtIDName;
    }

    public void setGovtIDName(String govtIDName) {
        this.govtIDName = govtIDName;
    }

    public String getGovtIDNumber() {
        return govtIDNumber;
    }

    public void setGovtIDNumber(String govtIDNumber) {
        this.govtIDNumber = govtIDNumber;
    }

    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

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
